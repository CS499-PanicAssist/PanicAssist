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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton phoneImage = (ImageButton) findViewById(R.id.phonebutton);

        phoneImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhoneNumbersActivity.class);
                startActivity(intent);
            }
        });

        ImageView alertImage = (ImageView) findViewById(R.id.alertImageView);

        if (alertAbleToSend()){
            alertImage.setImageResource(R.drawable.savemelogo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
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
        } else if (id == R.id.action_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.dialog_about_body)
                    .setTitle(R.string.dialog_about_title)
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog dialog = builder.create();

            dialog.show();
            return true;
        } else if (id == R.id.headphones_menu_checkbox){

            if (item.isChecked()){
                sentAlertOnUnplug(false);
                item.setChecked(false);
            }
            else {
                sentAlertOnUnplug(true);
                item.setChecked(true);
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

    private void sentAlertOnUnplug(boolean turnFeatureOn){
        //TODO if true have app send alert if headphones unplug
        //else turn off that feature
        //and save it to file

    }

    public boolean sendDefaultAlert(){
        //TODO implement

        Toast.makeText(getApplicationContext(), "Not implemented yet, but will be!", Toast.LENGTH_SHORT).show();
        return false;
    }
}
