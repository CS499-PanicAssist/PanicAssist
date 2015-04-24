package edu.cpp.preston.saveme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout accountLayout = (LinearLayout) findViewById(R.id.accountSetting);
        LinearLayout emergencyNumbersLayout = (LinearLayout) findViewById(R.id.numbersSetting);
        LinearLayout contactsLayout = (LinearLayout) findViewById(R.id.contactsSetting);
        LinearLayout quickMessagesLayout = (LinearLayout) findViewById(R.id.quickMessagesSetting);

        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });

        quickMessagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuickMessagesActivity.class);
                startActivity(intent);
            }
        });

        emergencyNumbersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmergencyNumbersActivity.class);
                startActivity(intent);
            }
        });

        contactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    private Activity getActivity(){
        return this;
    }
}
