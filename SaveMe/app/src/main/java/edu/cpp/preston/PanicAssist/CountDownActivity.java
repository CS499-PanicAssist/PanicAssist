package edu.cpp.preston.PanicAssist;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class CountDownActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //TODO display timerdialog over keyguard lock if possible

        setContentView(R.layout.activity_countdown);

        StaticMethods.showTimerDialog(this, getIntent().getIntExtra("time", 30));
    }
}
