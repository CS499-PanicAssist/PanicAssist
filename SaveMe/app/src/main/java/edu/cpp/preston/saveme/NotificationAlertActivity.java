package edu.cpp.preston.saveme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
                //TODO delete notification and go to mainActivity
                //intent.getStringExtra("ID") //id of notification shown
            }
        });

        TextView GPScoordsText = (TextView) findViewById(R.id.GPScoords);
        GPScoordsText.setText("Latitude: " + intent.getStringExtra("lat") + "\nLongitude: " + intent.getStringExtra("lon"));

        TextView whoWhenText = (TextView) findViewById(R.id.whoWhenText);
        whoWhenText.setText(intent.getStringExtra("name") + " sent an alert at " + intent.getStringExtra("time") + " on " + intent.getStringExtra("date"));

        TextView messageText = (TextView) findViewById(R.id.messageText);
        messageText.setText(intent.getStringExtra("message") + "");
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
}
