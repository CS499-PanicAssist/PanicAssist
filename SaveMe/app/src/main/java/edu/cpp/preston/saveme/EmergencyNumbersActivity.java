package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EmergencyNumbersActivity extends ActionBarActivity {

    private ArrayList<PhoneNumber> phoneNumbers;
    private PhoneNumberAdapter phoneNumberListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_numbers);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        phoneNumbers = new ArrayList<>(); //TODO get numbers from file here
        phoneNumbers.add(new PhoneNumber("Emergency Personal 1", "1(626)-330-3983"));
        phoneNumbers.add(new PhoneNumber("Emergency Personal 2", "1(626)-330-3983"));
        phoneNumbers.add(new PhoneNumber("Emergency Personal 3", "1(626)-330-3983"));

        ListView listView = (ListView) findViewById(R.id.EmergencyPhoneNumbersListView);
        phoneNumberListAdapter = new PhoneNumberAdapter(this, phoneNumbers);
        listView.setAdapter(phoneNumberListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_menu_call);
                builder.setMessage(phoneNumbers.get(i).getName() + "\n" + phoneNumbers.get(i).getNumber());

                //okay
                builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                //delete number
                builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO remove phone number from file
                        phoneNumbers.remove(i);
                        phoneNumberListAdapter.notifyDataSetChanged();
                    }
                });

                builder.create().show();
            }
        });

        ImageButton addImageButton = (ImageButton) findViewById(R.id.addNumberImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText = (EditText) findViewById(R.id.phoneNumberNameText);
                EditText numberText = (EditText) findViewById(R.id.phoneNumberText);

                if(nameText.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter a name", Toast.LENGTH_SHORT).show();
                    return;
                } else if(numberText.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter a phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO Check if phone number is a duplicate here?
                //TODO add to file system here

                phoneNumbers.add(new PhoneNumber(nameText.getText().toString(), numberText.getText().toString()));
                phoneNumberListAdapter.notifyDataSetChanged();

                nameText.setSelection(0);
                nameText.setText("");
                numberText.setText("");
            }
        });

    }

    private Activity getActivity(){
        return this;
    }

}
