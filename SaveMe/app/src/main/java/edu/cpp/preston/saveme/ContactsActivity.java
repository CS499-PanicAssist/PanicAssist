package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.ArrayList;

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

                contacts.add(new Contact(sharedPrefContacts.getString("displayname" + i,"ERROR"), sharedPrefContacts.getString("usernameOrNumber" + i,"ERROR"),isNumber, isConfirmed));
            }
        }

        final ListView listView = (ListView) findViewById(R.id.EmergencyPhoneNumbersListView);
        contactListAdapter = new ContactAdapter(this, contacts, false);
        listView.setAdapter(contactListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(contacts.get(i).getdisplayName() + "\n" + contacts.get(i).getID());

                //okay
                builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                //delete number
                builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        for (int j = 0; j < 50; j++){ //removes contact from preferences
                            if (sharedPrefContacts.getString("usernameOrNumber" + j, "*").equalsIgnoreCase(contacts.get(i).getID())){
                                SharedPreferences.Editor editor = sharedPrefContacts.edit();
                                editor.remove("displayname" + j);
                                editor.remove("usernameOrNumber" + j);
                                editor.remove("isNumber" + j);
                                editor.remove("isConfirmed" + j);
                                editor.remove("userObjectId" + j);
                                editor.commit();
                                break;
                            }
                        }

                        //TODO if contact is not confirmed delete notification on server here?

                        contacts.remove(i);
                        contactListAdapter.notifyDataSetChanged();
                    }
                });

                builder.create().show();
            }
        });

        final RadioButton phoneNumberRadio = (RadioButton) findViewById(R.id.phoneNumberRadioButton);
        RadioButton userNameRadio = (RadioButton) findViewById(R.id.userNameRadioButton);
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

                Contact newContact = new Contact(nameText.getText().toString(), numberOrUserText.getText().toString(), phoneNumberRadio.isChecked(), false);
                SharedPreferences sharedPrefSettings = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_general_settings_key), Context.MODE_PRIVATE);

                if (sharedPrefSettings.contains("username")){
                    for (int i = 0; i < 50; i++){ //add contact to prefecences
                        if (!sharedPrefContacts.contains("displayname" + i)){
                            SharedPreferences.Editor editor = sharedPrefContacts.edit();
                            editor.putString("displayname" + i, newContact.getdisplayName());
                            editor.putString("usernameOrNumber" + i, newContact.getID());

                            if (newContact.isNumber()){
                                editor.putString("isNumber" + i, "true");
                                editor.putString("isConfirmed" + i, "true");
                            } else{
                                editor.putString("isNumber" + i, "false");
                                editor.putString("isConfirmed" + i, "false"); //a contact is never confirmed when first added

                                ParseObject request = new ParseObject("Notification"); //make new notification
                                request.put("sender", sharedPrefSettings.getString("username", "ERROR"));
                                request.put("type", "request");
                                request.put("receiver", numberOrUserText.getText().toString());
                                request.put("senderId", sharedPrefSettings.getString("userObjectId", "*"));
                                request.saveEventually(); //save on server
                            }

                            editor.commit();
                            break;
                        }
                    }

                    contacts.add(newContact);
                    contactListAdapter.notifyDataSetChanged();
                    nameText.setText("");
                    numberOrUserText.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Must choose an account username first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Activity getActivity(){
        return this;
    }
}
