package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone_numbers, menu);

        ///////////////////////////////////////////////////
        phoneNumbers = new ArrayList<PhoneNumber>();
        phoneNumbers.add(new PhoneNumber("Emergency Personal 1", "1(626)-330-3983"));
        phoneNumbers.add(new PhoneNumber("Emergency Personal 2", "1(626)-330-3983"));
        phoneNumbers.add(new PhoneNumber("Emergency Personal 3", "1(626)-330-3983"));

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

                final AlertDialog dialog = builder.create();

                dialog.show();
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
                        deleteNumber();
                        //phoneNumbers.remove(i);
                        //TODO notify list view of data set change after number is removed
                        //listView.notifyDataSetChanged();
                    }
                });

                //Cancel
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                final AlertDialog dialog = builder.create();

                dialog.show();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Activity getActivity(){
        return this;
    }

    private void deleteNumber(){
        //TODO should delete number from saved file system
    }
}
