package edu.cpp.preston.PanicAssist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomizeAlert extends ActionBarActivity {

    private ArrayList<String> quickTexts;
    private ArrayList<Contact> contacts;
    private ContactAdapter contactListAdapter;
    private SharedPreferences sharedPrefContacts;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_alert);

        gps = new GPSTracker(this);
        final SharedPreferences sharedPrefQuickText = this.getSharedPreferences(getString(R.string.preference_file_quick_text_key), Context.MODE_PRIVATE);
        sharedPrefContacts = this.getSharedPreferences(getString(R.string.preference_file_contacts_key), Context.MODE_PRIVATE);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final EditText messageText = (EditText) findViewById(R.id.messageText);
        messageText.setText(sharedPrefQuickText.getString("quicktext0", ""));


        quickTexts = new ArrayList<String>();
        for (int i = 0; i < 50; i++){ //gets preferences
            if (sharedPrefQuickText.contains("quicktext" + i)){
                quickTexts.add(sharedPrefQuickText.getString("quicktext" + i,"ERROR"));
            }
        }

        final Button quickTextButton = (Button) findViewById(R.id.quickTextButton);
        quickTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                String[] shortMessages = new String[quickTexts.size()];
                final int MAX_SHOWN_CHAR_LENGTH = 30;

                for (int i = 0; i < quickTexts.size(); i++) {
                    if (quickTexts.get(i).length() > MAX_SHOWN_CHAR_LENGTH) {
                        shortMessages[i] = quickTexts.get(i).substring(0, MAX_SHOWN_CHAR_LENGTH) + "...";
                    } else {
                        shortMessages[i] = quickTexts.get(i);
                    }
                }

                builder.setTitle("Choose a message")
                        .setItems(shortMessages, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                messageText.setText(quickTexts.get(which)); //sets message text to quick text user has selected
                            }
                        });

                builder.create().show();
            }
        });

        contacts = new ArrayList<>();
        for (int i = 0; i < 50; i++){ //gets preferences
            if (sharedPrefContacts.contains("displayname" + i)){
                Contact newContact;

                if (sharedPrefContacts.getString("isNumber" + i,"ERROR").equalsIgnoreCase("true")){ //phone number
                    newContact = new ContactPhone(sharedPrefContacts.getString("displayname" + i,"ERROR"), sharedPrefContacts.getString("usernameOrNumber" + i,"ERROR"));
                } else { //username
                    if (sharedPrefContacts.getString("isConfirmed" + i,"ERROR").equalsIgnoreCase("true")){
                        newContact = new ContactSaveMe(sharedPrefContacts.getString("displayname" + i,"ERROR"), sharedPrefContacts.getString("usernameOrNumber" + i,"ERROR"), true);
                    } else{
                        newContact = new ContactSaveMe(sharedPrefContacts.getString("displayname" + i,"ERROR"), sharedPrefContacts.getString("usernameOrNumber" + i,"ERROR"), false);
                    }

                    newContact.setContactId(sharedPrefContacts.getString("userObjectId" + i, ""));
                }

                contacts.add(newContact);
            }
        }

        ListView listView = (ListView) findViewById(R.id.contactsListView);
        contactListAdapter = new ContactAdapter(this, contacts, true);
        listView.setAdapter(contactListAdapter);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCustomAlert(messageText.getText().toString());
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customize_alert, menu);
        return true;
    }

    public Activity getActivity(){
        return this;
    }

    public void sendCustomAlert(String userMessage){

        if (gps.canGetLocation()){
            String lat = gps.getLatitude() + "";
            String lon = gps.getLongitude() + "";
            String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + "(" + App.username + ")";
            String myLocation = "My location is: " + geoUri;
            DateFormat df = new SimpleDateFormat("h:mm a");
            String time = df.format(Calendar.getInstance().getTime());
            df = new SimpleDateFormat("MM/dd/yy");
            String date = df.format(Calendar.getInstance().getTime());

            for (Contact contact : contactListAdapter.contacts){
                if (contact.isSelected()) {
                    if (contact.getClass() == ContactPhone.class) { //send text here
                        CompatibilitySmsManager smsManager = CompatibilitySmsManager.getDefault();
                        smsManager.sendTextMessage("+" + contact.getUsernameOrNumber(), null, userMessage, null, null);
                        smsManager.sendTextMessage("+" + contact.getUsernameOrNumber(), null, myLocation, null, null);
                    } else { //send save me alert here
                        ParseObject notification = new ParseObject("Notification"); //make new notification
                        notification.put("sender", App.username);
                        notification.put("type", "alert");
                        notification.put("receiverId", contact.getContactId());
                        notification.put("message", userMessage);
                        notification.put("lat", lat);
                        notification.put("lon", lon);
                        notification.put("time", time);
                        notification.put("date", date);
                        notification.saveEventually(); //save notification on server

                        //TODO send push notification to alert user of new alert
                    }
                }
            }

            Toast.makeText(this, "Alerts sent!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unable to get location!", Toast.LENGTH_SHORT).show();
        }
    }
}
