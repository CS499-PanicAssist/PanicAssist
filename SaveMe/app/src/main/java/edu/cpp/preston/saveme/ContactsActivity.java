package edu.cpp.preston.saveme;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends ActionBarActivity {

    private ArrayList<Contact> contacts;
    private ContactAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences sharedPrefContacts = this.getSharedPreferences(getString(R.string.preference_file_contacts_key), Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        contacts = new ArrayList<>();
        for (int i = 0; i < 50; i++){ //gets preferences
            if (sharedPrefContacts.contains("displayname" + i)){
                Contact newContact;

                if (sharedPrefContacts.getString("isNumber" + i,"ERROR").equalsIgnoreCase("true")){
                    newContact = new ContactPhone(sharedPrefContacts.getString("displayname" + i,"ERROR"), sharedPrefContacts.getString("usernameOrNumber" + i,"ERROR"));
                } else {
                    boolean isConfirmed = false;

                    if (sharedPrefContacts.getString("isConfirmed" + i,"ERROR").equalsIgnoreCase("true"))
                        isConfirmed = true;

                    newContact = new ContactSaveMe(sharedPrefContacts.getString("displayname" + i,"ERROR"), sharedPrefContacts.getString("usernameOrNumber" + i,"ERROR"), isConfirmed);
                }

                contacts.add(newContact);
            }
        }

        final ListView listView = (ListView) findViewById(R.id.ContactsListView);
        contactListAdapter = new ContactAdapter(this, contacts, false);
        listView.setAdapter(contactListAdapter);

        final RadioButton phoneNumberRadio = (RadioButton) findViewById(R.id.phoneNumberRadioButton);
        final RadioButton userNameRadio = (RadioButton) findViewById(R.id.userNameRadioButton);
        final EditText nameText = (EditText) findViewById(R.id.contactNameText);
        final EditText numberOrUserText = (EditText) findViewById(R.id.contactNumberOrUserText);
        ImageButton addContactImageButton = (ImageButton) findViewById(R.id.addContactImageButton);

        phoneNumberRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOrUserText.setHint("Phone Number");
                numberOrUserText.setInputType(InputType.TYPE_CLASS_PHONE);
            }
        });

        userNameRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOrUserText.setHint("User Name");
                numberOrUserText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        addContactImageButton.setOnClickListener(new View.OnClickListener() {
            Contact toAdd;

            @Override
            public void onClick(View v) {

                if(nameText.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter a name", Toast.LENGTH_SHORT).show();
                    return;
                } else if(numberOrUserText.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter a phone number or user name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phoneNumberRadio.isChecked() && numberOrUserText.getText().toString().replaceAll("[^0-9]", "").length() < 10){
                    Toast.makeText(getApplicationContext(), "Phone number must include area code", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phoneNumberRadio.isChecked() && numberOrUserText.getText().toString().replaceAll("[^0-9]","").length() < 11){
                    numberOrUserText.setText("1" + numberOrUserText.getText());
                } else if (!phoneNumberRadio.isChecked() && numberOrUserText.getText().toString().length() < 5){
                    Toast.makeText(getApplicationContext(), "Username too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phoneNumberRadio.isChecked()){
                    toAdd = new ContactPhone(nameText.getText().toString(), numberOrUserText.getText().toString());
                } else {
                    toAdd = new ContactSaveMe(nameText.getText().toString(), numberOrUserText.getText().toString(), false);
                }

                if (App.username.length() > 4){

                    final SharedPreferences.Editor editor = sharedPrefContacts.edit();

                    if (toAdd.getClass() == ContactSaveMe.class){ //a user
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

                        query.whereEqualTo("username", toAdd.getUsernameOrNumber());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> queryNotificationList, ParseException e) {
                                if (e == null) {
                                    if (queryNotificationList.size() == 0){
                                        Toast.makeText(getApplicationContext(), "No user with that username currently", Toast.LENGTH_SHORT).show();
                                    } else{
                                        for (int i = 0; i < 50; i++) { //add contact to prefecences
                                            if (!sharedPrefContacts.contains("displayname" + i)) {
                                                editor.putString("displayname" + i, toAdd.getDisplayName());
                                                editor.putString("usernameOrNumber" + i, toAdd.getUsernameOrNumber());
                                                editor.putString("isNumber" + i, "false");
                                                editor.putString("isConfirmed" + i, "false"); //a contact is never confirmed when first added

                                                ParseObject request = new ParseObject("Notification"); //make new notification
                                                request.put("sender", App.username);
                                                request.put("type", "request");
                                                request.put("receiver", numberOrUserText.getText().toString());
                                                request.put("senderId", App.userId);
                                                request.saveEventually(); //save on server

                                                editor.commit();

                                                contacts.add(toAdd);
                                                contactListAdapter.notifyDataSetChanged();
                                                nameText.setText("");
                                                numberOrUserText.setText("");
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error contacting server", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else { //a phone number
                        for (int i = 0; i < 50; i++){ //add contact to prefecences
                            if (!sharedPrefContacts.contains("displayname" + i)){
                                editor.putString("displayname" + i, toAdd.getDisplayName());
                                editor.putString("usernameOrNumber" + i, toAdd.getUsernameOrNumber());
                                editor.putString("isNumber" + i, "true");
                                editor.putString("isConfirmed" + i, "true"); //not needed
                                editor.commit();
                                contacts.add(toAdd);
                                contactListAdapter.notifyDataSetChanged();
                                nameText.setText("");
                                numberOrUserText.setText("");
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Must choose an account username first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
