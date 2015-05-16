package edu.cpp.preston.PanicAssist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class HeadsetStateBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        // Wired headset monitoring
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {

            final int state = intent.getIntExtra("state", 0);

            SharedPreferences sharedPrefSettings = context.getSharedPreferences("edu.cpp.PanicAssist.PREFERENCE_GENERAL_KEY", Context.MODE_PRIVATE);

            System.out.println("hello " + sharedPrefSettings.getString("jack", "*"));

            if (sharedPrefSettings.getString("jack", "off").equalsIgnoreCase("on")){ // only set if preference has alert on jack set to on
                if (state > 0){
                    Toast.makeText(context, "Panic Assist on unplug enabled", Toast.LENGTH_LONG).show();
                } else {

                    //wake scree and show dialog here
                    //TODO
                    //StaticMethods.showTimerDialog(context.acti, Integer.parseInt(sharedPrefSettings.getString("jackDelay", "30")));

                }
            }

        }
    }
}