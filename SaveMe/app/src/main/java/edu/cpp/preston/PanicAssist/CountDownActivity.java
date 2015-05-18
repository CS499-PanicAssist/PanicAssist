package edu.cpp.preston.PanicAssist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

public class CountDownActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        super.onCreate(savedInstanceState);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl;
        wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(40000);

        StaticMethods.showTimerDialog(this, getIntent().getIntExtra("time", 30));
    }
}
