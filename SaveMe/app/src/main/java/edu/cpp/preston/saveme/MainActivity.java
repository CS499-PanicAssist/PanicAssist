package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends ActionBarActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private static ArrayList<Notification> notifications;
    private static NotificationAdapter notificationListAdapter;
    static SharedPreferences sharedPrefNotifications;
    static SharedPreferences sharedPrefSettings;
    static SharedPreferences sharedPrefContacts;
    static SharedPreferences.Editor notificationEditor;
    static SharedPreferences sharedPrefQuickTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefSettings = this.getSharedPreferences(getString(R.string.preference_file_general_settings_key), Context.MODE_PRIVATE);
        sharedPrefNotifications = this.getSharedPreferences(getString(R.string.preference_file_notifications_key), Context.MODE_PRIVATE);
        sharedPrefContacts = this.getSharedPreferences(getString(R.string.preference_file_contacts_key), Context.MODE_PRIVATE);
        sharedPrefQuickTexts = this.getSharedPreferences(getString(R.string.preference_file_quick_text_key), Context.MODE_PRIVATE);

        notificationEditor = sharedPrefNotifications.edit();
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

        //TODO in background get notifications and then refresh adapater once added to notifications array
        //TODO cont... notification key, the value should be either "info" "request" or "alert" and the other data stored is differnt on each type

        reloadNotificationListView();
    }

    private void reloadNotificationListView() {
        notifications = new ArrayList<Notification>();

        if (!sharedPrefNotifications.contains("welcome")){ //if first launch then add welcome notification
            notificationEditor.putString("welcome", getResources().getString(R.string.welcomeMessage));
            notificationEditor.commit();
        }

        if (!sharedPrefNotifications.getString("welcome", "*").equals("*")){
            notifications.add(new NotificationInfo(getResources().getString(R.string.welcome), getResources().getString(R.string.welcomeMessage)));
        }

        for (int i = 0; i < 50; i++){ //gets notifications from preferences file
            if (sharedPrefNotifications.contains("notification" + i)){
                if (sharedPrefNotifications.getString("notification" + i, "ERROR").equalsIgnoreCase("info")){
                    notifications.add(new NotificationInfo(sharedPrefNotifications.getString("title" + i, "ERROR"), sharedPrefNotifications.getString("message" + i, "ERROR") ));
                } else if (sharedPrefNotifications.getString("notification" + i, "ERROR").equalsIgnoreCase("request")){
                    notifications.add(new NotificationRequest(sharedPrefNotifications.getString("sender" + i, "ERROR") ));
                } else if (sharedPrefNotifications.getString("notification" + i, "ERROR").equalsIgnoreCase("alert")){
                    notifications.add(new NotificationAlert(sharedPrefNotifications.getString("sender" + i, "ERROR"), sharedPrefNotifications.getString("personalMessage" + i, "ERROR"),
                            Double.parseDouble(sharedPrefNotifications.getString("latitude" + i, "ERROR")), Double.parseDouble(sharedPrefNotifications.getString("longitude" + i, "ERROR")),
                            sharedPrefNotifications.getString("time" + i, "ERROR"), sharedPrefNotifications.getString("date" + i, "ERROR")));
                }
            }
        }

        ListView listView = (ListView) findViewById(R.id.notificationListView);
        notificationListAdapter = new NotificationAdapter(this, notifications);
        listView.setAdapter(notificationListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showNotificationDialog(notifications.get(i)); //shows dialog for notification clicked
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
        int id = item.getItemId();

        if (id == R.id.action_settings) { //settings was selected in menu dropdown
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_help) { //help was selected in menu dropdown
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.headphones_menu_checkbox){
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
        }

        return super.onOptionsItemSelected(item);
    }

    //User clicks on alert image
    public void alertClick(View view) {

        if (!alertAbleToSend()){ //cannot send an alert
            Toast.makeText(getApplicationContext(), "Cannot send, please check settings!", Toast.LENGTH_SHORT).show();
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.send_default, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendDefaultAlert(getApplicationContext());
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
                    sendDefaultAlert(getApplicationContext());
                }
            }
        };

        dialog.show();
        timer.start();
    }

    private boolean alertAbleToSend(){ // returns true if username is chosen and has at least one confirmed contact
        String username = sharedPrefSettings.getString("username", "*");

        if (username.length() > 4) { // username has been set up
            for (int i = 0; i < 50; i++){ //gets notifications from preferences file
                if (sharedPrefContacts.contains("displayname" + i) && sharedPrefContacts.getString("isConfirmed" + i, "*").equalsIgnoreCase("true")){
                    return true;
                }
            }
        }

        return false;
    }

    private void showNotificationDialog(final Notification notification){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (notification.getClass() == NotificationInfo.class){ //info message

            builder.setMessage(notification.getMessage())
                    .setTitle(notification.getTitle())
                    .setIcon(android.R.drawable.ic_dialog_info);

            builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    if (notification.getTitle().equals(getResources().getString(R.string.welcome))) { //is welcome info
                        notificationEditor.putString("welcome", "*"); //make a "*" so that we know ist been viewed and deleted
                    } else { //is trying to delete info that is not welcome info

                        for (int j = 0; j < 50; j++) { //removes text from preferences
                            //note: notification id isnt necessary  since all alerts should be differnt somehow
                            if (sharedPrefNotifications.contains("notification" + j) && sharedPrefNotifications.getString("title" + j, "*").equalsIgnoreCase(notification.getTitle())) {
                                notificationEditor.remove("notification" + j);
                                notificationEditor.remove("title" + j);
                                notificationEditor.remove("message" + j);
                                notificationEditor.commit();
                                break;
                            }
                        }
                    }

                    notificationEditor.commit();
                    notifications.remove(notification);
                    notificationListAdapter.notifyDataSetChanged();

                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

        } else if (notification.getClass() == NotificationAlert.class){ //alert
            builder.setMessage(notification.getMessage())
                    .setTitle(notification.getTitle())
                    .setIcon(android.R.drawable.ic_dialog_alert);

            builder.setPositiveButton(R.string.viewAlert, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(MainActivity.this, NotificationAlertActivity.class);
                    NotificationAlert notification2 = (NotificationAlert) notification;

                    intent.putExtra("message", notification2.getMessage());
                    intent.putExtra("personalMessage", notification2.getPersonalMessage());
                    intent.putExtra("lat", notification2.getLat() + "");
                    intent.putExtra("lon", notification2.getLon() + "");

                    startActivity(intent);
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

        } else if (notification.getClass() == NotificationRequest.class){ //contact request

            builder.setMessage(notification.getMessage())
                    .setTitle(notification.getTitle())
                    .setIcon(android.R.drawable.ic_dialog_email);

            builder.setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO send alert to other user that they can now alert this user

                    deleteNotificationRequest(notification); //deletes notification from memory
                }
            });

            builder.setNeutralButton(R.string.ignore, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    deleteNotificationRequest(notification); //deletes notification from memory
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

        } else {
            throw new RuntimeException("Notification type not recognized.");
        }

        builder.create().show();
    }

    private void deleteNotificationRequest(Notification notification){
        for (int j = 0; j < 50; j++) { //removes request from preferences
            if (sharedPrefNotifications.getString("notification" + j, "*").equalsIgnoreCase("request") && sharedPrefNotifications.getString("sender" + j, "*").equalsIgnoreCase(notification.getSender())) {
                notificationEditor.remove("notification" + j);
                notificationEditor.remove("sender" + j);
                notificationEditor.commit();
                break;
            }
        }

        notifications.remove(notification);
        notificationListAdapter.notifyDataSetChanged();
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

    public boolean sendDefaultAlert(Context context){
        //TODO implement

        //Todo delete between these lines----------------------------------------------
        Notification hi = new NotificationAlert("hotrod2", "Looks like i might need sum help!", 40.712312, -74.0057372, "10:30PM", "10/10/15");
        notifications.add(hi);
        notifications.add(new NotificationRequest("coolguy5"));
        notificationListAdapter.notifyDataSetChanged();
        notificationEditor.putString("notification1", "request");
        notificationEditor.putString("sender1", "coolguy5");

        notificationEditor.putString("notification0", "alert");
        notificationEditor.putString("sender0", "hotrod2");
        notificationEditor.putString("message0", hi.getMessage());
        notificationEditor.putString("personalMessage0", "Looks like i might need sum help!");
        notificationEditor.putString("latitude0", "40.712312");
        notificationEditor.putString("longitude0", "-74.0057372");
        notificationEditor.putString("time0", "10:30PM");
        notificationEditor.putString("date0", "10/10/15");

        notificationEditor.commit();
        /////till here----------------------------------------------------------------


        for (int j = 0; j < 50; j++) { //removes request from preferences
            if (sharedPrefContacts.contains("displayname" + j)) {
                String userMessage = sharedPrefQuickTexts.getString("quicktext0", ""); //TODO get user-set default message
                String lat = "40.712312";
                String lon = "-74.0057372";
                String name = sharedPrefSettings.getString("username", "No username set");
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + "(" + name + ")";
                String fullSMS = userMessage + " My location is: " + geoUri;
                DateFormat df = new SimpleDateFormat("h:mm a");
                String time = df.format(Calendar.getInstance().getTime());
                df = new SimpleDateFormat("MM/dd/yy");
                String date = df.format(Calendar.getInstance().getTime());

                if (sharedPrefContacts.getString("isConfirmed" + j, "*").equalsIgnoreCase("true")){
                    if (sharedPrefContacts.getString("isNumber" + j, "*").equalsIgnoreCase("true")){ //send text here
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("+" + sharedPrefContacts.getString("usernameOrNumber" + j, "ERROR"), null , fullSMS, null, null);
                    } else{
                        ParseObject notification = new ParseObject("Notification"); //make new notification
                        notification.put("sender", sharedPrefSettings.getString("username", "no username"));
                        notification.put("receiver", sharedPrefContacts.getString("usernameOrNumber" + j, "ERROR"));
                        notification.put("message", userMessage);
                        notification.put("lat", lat);
                        notification.put("lon", lon);
                        notification.put("time", time);
                        notification.put("date", date);
                        notification.saveInBackground(); //save notification on server

                        //TODO send push notification to alert user of new alert
                    }
                }
            }
        }

        Toast.makeText(context, "Alerts sent!", Toast.LENGTH_SHORT).show();
        return false;
    }
}
