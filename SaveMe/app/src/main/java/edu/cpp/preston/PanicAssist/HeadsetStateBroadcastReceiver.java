package edu.cpp.preston.PanicAssist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.widget.Toast;

public class HeadsetStateBroadcastReceiver extends BroadcastReceiver {

    boolean wasConnected = false;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        // Wired headset monitoring
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {

            final int state = intent.getIntExtra("state", 0);

            SharedPreferences sharedPrefSettings = context.getSharedPreferences("edu.cpp.PanicAssist.PREFERENCE_GENERAL_KEY", Context.MODE_PRIVATE);

            if (sharedPrefSettings.getString("jack", "off").equalsIgnoreCase("on")){ // only set if preference has alert on jack set to on
                if (state > 0){
                    wasConnected = true;
                    Toast.makeText(context, "Panic Assist on unplug enabled", Toast.LENGTH_LONG).show();
                } else if (wasConnected){
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(1000);

                    Intent i = new Intent(context, CountDownActivity.class);
                    i.putExtra("time", sharedPrefSettings.getInt("jackDelay", 30));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }
            }
        }
    }
}