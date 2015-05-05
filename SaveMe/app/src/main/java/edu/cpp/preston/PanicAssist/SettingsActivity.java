package edu.cpp.preston.PanicAssist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    static SharedPreferences sharedPrefSettings;
    static SharedPreferences.Editor editorSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPrefSettings = this.getSharedPreferences(getString(R.string.preference_file_general_settings_key), Context.MODE_PRIVATE);
        editorSettings = sharedPrefSettings.edit();
        LinearLayout accountLayout = (LinearLayout) findViewById(R.id.accountSetting);
        LinearLayout emergencyNumbersLayout = (LinearLayout) findViewById(R.id.numbersSetting);
        LinearLayout contactsLayout = (LinearLayout) findViewById(R.id.contactsSetting);
        LinearLayout quickMessagesLayout = (LinearLayout) findViewById(R.id.quickMessagesSetting);
        final Spinner delayTimeSpinner = (Spinner) findViewById(R.id.delayTimeSpinner);
        TextView userName = (TextView) findViewById(R.id.accountName);
        CheckBox jackCheckbox = (CheckBox) findViewById(R.id.jackCheckbox);
        delayTimeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_delay_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        delayTimeSpinner.setAdapter(adapter);

        delayTimeSpinner.setSelection(sharedPrefSettings.getInt("jackPosition", 2));

        if (sharedPrefSettings.getString("jack", "off").equalsIgnoreCase("on")){
            jackCheckbox.setChecked(true);
        } else {
            delayTimeSpinner.setEnabled(false);
        }

        jackCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    delayTimeSpinner.setEnabled(true);
                    editorSettings.putString("jack", "on");
                } else{
                    delayTimeSpinner.setEnabled(false);
                    editorSettings.putString("jack", "off");
                }
                editorSettings.commit();
            }
        });

        if (App.username.length() > 4){ //if username has been created before
            userName.setText(App.username);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        editorSettings.putInt("jackPosition", position);
        editorSettings.putInt("jackDelay", secondsDelay(position));
        editorSettings.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private int secondsDelay(int position){
        switch (position) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 60;
            case 4:
                return 5 * 60;
            case 5:
                return 10 * 60;
        }

        return 30;
    }
}
