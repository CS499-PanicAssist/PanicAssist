package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
    private LocationManager locationManager;
    private boolean isChecked[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_alert);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final SharedPreferences sharedPrefQuickText = getActivity().getSharedPreferences(getString(R.string.preference_file_quick_text_key), Context.MODE_PRIVATE);
        final SharedPreferences sharedPrefContacts = this.getSharedPreferences(getString(R.string.preference_file_contacts_key), Context.MODE_PRIVATE);

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

        contacts = new ArrayList<Contact>();
        for (int i = 0; i < 50; i++){ //gets preferences
            if (sharedPrefContacts.contains("displayname" + i)){
                boolean isNumber = false, isConfirmed = false;

                if (sharedPrefContacts.getString("isNumber" + i,"ERROR").equalsIgnoreCase("true")){
                    isNumber = true;
                }

                if (sharedPrefContacts.getString("isConfirmed" + i,"ERROR").equalsIgnoreCase("true")){
                    isConfirmed = true;
                }

                if (isNumber || isConfirmed){ //must be confirmed to show on send alert to list
                    Contact newContact = new Contact(sharedPrefContacts.getString("displayname" + i,"ERROR"), sharedPrefContacts.getString("usernameOrNumber" + i,"ERROR"),isNumber, isConfirmed);
                    newContact.setContactId(sharedPrefContacts.getString("userObjectId" + i, ""));
                    contacts.add(newContact);
                }
            }
        }

        ListView listView = (ListView) findViewById(R.id.contactsListView);
        //listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);


        contactListAdapter = new ContactAdapter(this, contacts, true);
        listView.setAdapter(contactListAdapter);



        /*
        //HELPpPPPP VVVVVVVVVVVVVVVVVVVVVVVVV

        isChecked = new boolean[contacts.size()];
        for (int i = 0; i < isChecked.length; i++){
            isChecked[i] = true;
        }

        //HELP111111111111111111111111111111 ^^^^^6

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.contactCheckBox);

                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    isChecked[i] = false;
                } else {
                    checkBox.setChecked(true);
                    isChecked[i] = true;
                }
            }
        });
*/

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAlert(messageText.getText().toString());
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

    public void sendAlert(String userMessage){

        Criteria criteria = new Criteria();
        SharedPreferences sharedPrefSettings = this.getSharedPreferences(getString(R.string.preference_file_general_settings_key), Context.MODE_PRIVATE);
        String username = sharedPrefSettings.getString("username", "*");
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        String lat = location.getLatitude() + "";
        String lon = location.getLongitude() + "";
        String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + "(" + username + ")";
        String fullSMS = userMessage + " My location is: " + geoUri;
        DateFormat df = new SimpleDateFormat("h:mm a");
        String time = df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("MM/dd/yy");
        String date = df.format(Calendar.getInstance().getTime());

        if (username.length() < 4){
            Toast.makeText(getApplicationContext(), "Get a username first!", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < contactListAdapter.getCount(); i++){
                View view = contactListAdapter.getView(i, null, null); //hardly right!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.contactCheckBox);

                if (checkBox.isChecked()){

                    if (contacts.get(i).isNumber()){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("+" + contacts.get(i).getID(), null , fullSMS, null, null);
                    } else {
                        ParseObject notification = new ParseObject("Notification"); //make new notification
                        notification.put("sender", username);
                        notification.put("type", "alert");
                        notification.put("receiverId", contacts.get(i).getContactId());
                        notification.put("message", userMessage);
                        notification.put("lat", lat);
                        notification.put("lon", lon);
                        notification.put("time", time);
                        notification.put("date", date);
                        notification.saveEventually(); //save notification on server
                    }
                }
            }

            Toast.makeText(getApplicationContext(), "Alerts sent!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
