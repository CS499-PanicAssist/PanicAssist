package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomizeAlert extends ActionBarActivity {

    private ArrayList<Contact> contacts;
    private ContactAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_alert);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAlert();
            }
        });

        Button quickTextButton = (Button) findViewById(R.id.quickTextButton);
        quickTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final String[] fullMessages = new String[3]; //TODO get user saved messaged and set to messages[]
                fullMessages[0] = "Help I'm in trouble";
                fullMessages[1] = "Please call me, if I don't pick up please come to my location";
                fullMessages[2] = "This is just a test message";

                String[] shortMessages = new String[3];
                final int MAX_SHOWN_CHAR_LENGTH = 30;

                for(int i = 0; i <fullMessages.length; i++){
                    if (fullMessages[i].length() > MAX_SHOWN_CHAR_LENGTH){
                        shortMessages[i] = fullMessages[i].substring(0, MAX_SHOWN_CHAR_LENGTH) + "...";
                    } else{
                        shortMessages[i] = fullMessages[i];
                    }
                }

                builder.setTitle("Choose a message")
                        .setItems(shortMessages, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                TextView messageText = (TextView) findViewById(R.id.messageText);
                                messageText.setText(fullMessages[which]); //sets message text to quick text user has selected
                            }
                        });

                builder.create().show();
            }
        });

        contacts = new ArrayList<Contact>();
        contacts.add(new Contact("John Doe", "6262175613", true));
        contacts.add(new Contact("James Doe", "coolguy1", false));
        contacts.add(new Contact("James Doe", "coolguy2", false));
        contacts.add(new Contact("James Doe", "coolguy3", false));
        contacts.add(new Contact("James Doe", "coolguy4", false));
        contacts.add(new Contact("Dude Doe", "6262175613", true));

        final ListView listView = (ListView) findViewById(R.id.contactsListView);
        contactListAdapter = new ContactAdapter(this, contacts);
        listView.setAdapter(contactListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO check checkbox on listview item click
                /*
                Toast.makeText(view.getContext(), "view clicked", Toast.LENGTH_SHORT).show();


                CheckBox checkBox = (CheckBox) listView.findViewById(i).findViewById(R.id.checkbox);

                if (checkBox.isChecked()){
                    checkBox.setChecked(false);
                } else{
                    checkBox.setChecked(false);
                }

                checkBox.notify(); //needed?

*/
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

    public boolean sendAlert(){
        //TODO Send message according to inputs on this activity from the user
        Toast.makeText(getApplicationContext(), "Not implemented yet, but will be!", Toast.LENGTH_SHORT).show();

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
}
