package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;

public class ContactsActivity extends ActionBarActivity {

    private ArrayList<Contact> contacts;
    private ContactAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        contacts = new ArrayList<Contact>();
        contacts.add(new Contact("John Doe", "6262175613", true)); //TODO populate from file
        contacts.add(new Contact("James Doe", "coolguy1", false));
        contacts.add(new Contact("James Doe", "coolguy2", false));
        Contact hi = new Contact("James Doe", "coolguy3", false);
        hi.setIsConfirmed(true);
        contacts.add(hi);
        contacts.add(new Contact("James Doe", "coolguy4", false));
        contacts.add(new Contact("Dude Doe", "6262175613", true));

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
                        //TODO remove contact from file
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
                }

                //TODO Check if contact is a duplicate here?
                //TODO add to file system here

                contacts.add(new Contact(nameText.getText().toString(), numberOrUserText.getText().toString(), phoneNumberRadio.isChecked()));

                contactListAdapter.notifyDataSetChanged();

                nameText.setText("");
                numberOrUserText.setText("");
            }
        });

    }

    private Activity getActivity(){
        return this;
    }
}
