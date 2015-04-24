package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

public class PhoneNumbersActivity extends ActionBarActivity {

    private ArrayList<PhoneNumber> phoneNumbers;
    private PhoneNumberAdapter phoneNumberListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numbers);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final SharedPreferences sharedPrefPhoneNumbers = this.getSharedPreferences(getString(R.string.preference_file_phone_numbers_key), Context.MODE_PRIVATE);

        phoneNumbers = new ArrayList<PhoneNumber>();
        for (int i = 0; i < 50; i++){ //gets preferences
            if (sharedPrefPhoneNumbers.contains("name" + i)){
                phoneNumbers.add(new PhoneNumber(sharedPrefPhoneNumbers.getString("name" + i,"ERROR"), sharedPrefPhoneNumbers.getString("number" + i,"ERROR")));
            }
        }

        ListView listView = (ListView) findViewById(R.id.phoneNumberListView);
        phoneNumberListAdapter = new PhoneNumberAdapter(this, phoneNumbers);
        listView.setAdapter(phoneNumberListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_menu_call);
                builder.setMessage(phoneNumbers.get(i).getName() + "\n" + phoneNumbers.get(i).getNumber());

                //Call immediately
                builder.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumbers.get(i).getNumbersOnly()));
                        startActivity(intent);
                    }
                });

                //Open dialer with number
                builder.setNeutralButton(R.string.open_dialer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneNumbers.get(i).getNumbersOnly()));
                        startActivity(intent);
                    }
                });

                //Cancel phone number click
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                builder.create().show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setMessage("Delete " + phoneNumbers.get(i).getName() + "?");

                //Delete
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        for (int j = 0; j < 50; j++){ //removes number from preferences
                            if (sharedPrefPhoneNumbers.getString("name" + j, "*").equalsIgnoreCase(phoneNumbers.get(i).getName())){
                                SharedPreferences.Editor editor = sharedPrefPhoneNumbers.edit();
                                editor.remove("name" + j);
                                editor.remove("number" + j);
                                editor.commit();
                                break;
                            }
                        }

                        phoneNumbers.remove(i);
                        phoneNumberListAdapter.notifyDataSetChanged();
                    }
                });

                //Cancel
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                builder.create().show();

                return true;
            }
        });

        Button addNumberButton = (Button) findViewById(R.id.addPhoneNumber);
        addNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmergencyNumbersActivity.class);
                startActivity(intent);
            }
        });

    }

    private Activity getActivity(){
        return this;
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
