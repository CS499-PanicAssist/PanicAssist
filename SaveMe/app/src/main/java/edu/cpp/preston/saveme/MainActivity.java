package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private ArrayList<Notification> notifications;
    private NotificationAdapter notificationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton phoneImage = (ImageButton) findViewById(R.id.phonebutton);

        //sets phone image button click listener
        phoneImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhoneNumbersActivity.class);
                startActivity(intent);
            }
        });

        //makes alert image correct on load
        if (alertAbleToSend()){
            ImageView alertImage = (ImageView) findViewById(R.id.alertImageView);
            alertImage.setImageResource(R.drawable.savemelogo);
        }

        //sets up notification area
        notifications = new ArrayList<Notification>();
        notifications.add(new Notification(1,"Welcome", "This is a welcome message to new users"));
        notifications.add(new Notification(2, "John Doe", "Alert!", "This is an alert!", 0.0, 0.0));
        notifications.add(new Notification(3, "James Doe", "Contact Request!", "This is a request to add you as an alert contact", 0.0, 0.0));
        notifications.add(new Notification(3, "James Doe", "Contact Request!", "This is a request to add you as an alert contact", 0.0, 0.0));
        notifications.add(new Notification(3, "James Doe", "Contact Request!", "This is a request to add you as an alert contact", 0.0, 0.0));

        ListView listView = (ListView) findViewById(R.id.notificationListView);
        notificationListAdapter = new NotificationAdapter(this, notifications);
        listView.setAdapter(notificationListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                //intent.putExtra("name", friends.get(i).getName());
                //TODO put extra notificationId, -not yet made yet though
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO have popup dialog as to delete
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //makes jack menu icon correct on load
        if (sendAlertOnUnplug()) {
            MenuItem jackMenuItem = menu.findItem(R.id.headphones_menu_checkbox);
            jackMenuItem.setChecked(true);
            jackMenuItem.setIcon(R.drawable.jackon);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        //makes user press back twice to exit as a annoying safety feature
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //TODO make so that this works ^

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //settings was selected in menu dropdown
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            //help was selected in menu dropdown
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        /*
        //TODO place about in helps drop down bar
        else if (id == R.id.action_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.dialog_about_body)
                    .setTitle(R.string.dialog_about_title)
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog dialog = builder.create();

            dialog.show();
            return true;
        }
        */
        else if (id == R.id.headphones_menu_checkbox){
            if (item.isChecked()){ //turn off alert on unplug
                sendAlertOnUnplug(false);
                item.setChecked(false);
                item.setIcon(R.drawable.jack);
                Toast.makeText(getApplicationContext(), "Alert on audio unplug: OFF", Toast.LENGTH_SHORT).show();
            } else { //turn on alert on unplug
                sendAlertOnUnplug(true);
                item.setChecked(true);
                item.setIcon(R.drawable.jackon);
                Toast.makeText(getApplicationContext(), "Alert on audio unplug: ON", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //User clicks on alert image
    public void alertClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.send_default, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendDefaultAlert();
            }
        });

        builder.setNeutralButton(R.string.send_custom, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getActivity(), CustomizeAlert.class);
                startActivity(intent);
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.setMessage("Send default alert now?"); // Do not remove this line of code

        CountDownTimer timer = new CountDownTimer(20000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                dialog.setMessage("Send default alert now?\nSeconds until sent: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                if (dialog.isShowing()){ // Dialog box is still open after allotted time, therefor send alert
                    dialog.cancel();
                    sendDefaultAlert();
                }
            }
        };

        dialog.show();
        timer.start();

    }

    private boolean alertAbleToSend(){
        //TODO Check if alert is set up and would be able to be send if needed


        return true;
    }

    private Activity getActivity(){
        return this;
    }

    private void sendAlertOnUnplug(boolean turnFeatureOn){
        //TODO if true have app send alert if headphones unplug
        //else turn off that feature
        //and save it to file
    }

    private boolean sendAlertOnUnplug(){
        //TODO return true if should send alert on unplug

        return true;
    }

    public boolean sendDefaultAlert(){
        //TODO implement

        Toast.makeText(getApplicationContext(), "Not implemented yet, but will be!", Toast.LENGTH_SHORT).show();
        return false;
    }
}
