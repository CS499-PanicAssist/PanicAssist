package edu.cpp.preston.saveme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends ActionBarActivity {

    static SharedPreferences sharedPrefSettings;

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

        TextView userName = (TextView) findViewById(R.id.accountName);

        sharedPrefSettings = this.getSharedPreferences(getString(R.string.preference_file_general_settings_key), Context.MODE_PRIVATE);

        String username = sharedPrefSettings.getString("username", "*");
        if (username.length() > 4){ //if username has been created before
            userName.setText(username);
        }

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

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
