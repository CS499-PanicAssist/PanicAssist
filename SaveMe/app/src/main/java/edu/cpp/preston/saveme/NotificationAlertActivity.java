package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotificationAlertActivity extends ActionBarActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notfication_alert);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();

        Button openMapButton = (Button) findViewById(R.id.viewMapButton);

        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + intent.getStringExtra("lat") + "," + intent.getStringExtra("lon") + " (" + intent.getStringExtra("name") + " at " + intent.getStringExtra("time") + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });

        Button deleteNotification = (Button) findViewById(R.id.deleteNotificationButton);

        deleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.deleteNotificationQuestion);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final SharedPreferences sharedPrefNotifications = getActivity().getSharedPreferences(getString(R.string.preference_file_notifications_key), Context.MODE_PRIVATE);

                        int j = intent.getIntExtra("notificationNumber", -1);
                        SharedPreferences.Editor editor = sharedPrefNotifications.edit();
                        editor.remove("notification" + j);
                        editor.remove("sender" + j);
                        editor.remove("personalMessage" + j);
                        editor.remove("latitude" + j);
                        editor.remove("longitude" + j);
                        editor.remove("time" + j);
                        editor.remove("date" + j);
                        editor.commit();

                        NavUtils.navigateUpFromSameTask(getActivity());
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel
                    }
                });

                builder.create().show();
            }
        });

        TextView GPScoordsText = (TextView) findViewById(R.id.GPScoords);
        GPScoordsText.setText("Latitude: " + intent.getStringExtra("lat") + "\nLongitude: " + intent.getStringExtra("lon"));

        TextView whoWhenText = (TextView) findViewById(R.id.whoWhenText);
        whoWhenText.setText(intent.getStringExtra("message"));

        TextView messageText = (TextView) findViewById(R.id.messageText);
        messageText.setText(intent.getStringExtra("personalMessage") + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notfication, menu);
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

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
