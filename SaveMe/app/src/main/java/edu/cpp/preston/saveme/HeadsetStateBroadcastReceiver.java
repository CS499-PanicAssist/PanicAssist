package edu.cpp.preston.saveme;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class HeadsetStateBroadcastReceiver extends BroadcastReceiver {

    public static final String[] HEADPHONE_ACTIONS = {
            Intent.ACTION_HEADSET_PLUG,
            "android.bluetooth.headset.action.STATE_CHANGED",
            "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED"
    };

    @Override
    public void onReceive(final Context context, final Intent intent) {

        boolean broadcast = false;

        // Wired headset monitoring
        if (intent.getAction().equals(HEADPHONE_ACTIONS[0])) {
            final int state = intent.getIntExtra("state", 0);
            //AudioPreferences.setWiredHeadphoneState(context, state > 0);
            broadcast = true;
        }

        // Bluetooth monitoring
        // Works up to and including Honeycomb
        if (intent.getAction().equals(HEADPHONE_ACTIONS[1])) {
            int state = intent.getIntExtra("android.bluetooth.headset.extra.STATE", 0);
            //AudioPreferences.setBluetoothHeadsetState(context, state == 2);
            broadcast = true;
        }

        // Works for Ice Cream Sandwich
        if (intent.getAction().equals(HEADPHONE_ACTIONS[2])) {
            int state = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
            //AudioPreferences.setBluetoothHeadsetState(context, state == 2);
            broadcast = true;
        }

        // Used to inform interested activities that the headset state has changed
        if (broadcast) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("headsetStateChange"));


            Toast.makeText(context, "Not implemented yet, but will be!", Toast.LENGTH_SHORT).show();

            /*



            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton(R.string.send_default, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //MainActivity.sendDefaultAlert(getApplicationContext());
                }
            });

            builder.setNeutralButton(R.string.send_custom, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Intent intent = new Intent(getActivity(), CustomizeAlert.class);
                    //startActivity(intent);
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
                        //sendDefaultAlert(getApplicationContext());
                    }
                }
            };

            dialog.show();
            timer.start();




            */













        }

    }
}