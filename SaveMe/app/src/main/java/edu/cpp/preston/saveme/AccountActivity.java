package edu.cpp.preston.saveme;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);

        nameEditText.setText(getUserName());

        Button chooseNameButton = (Button) findViewById(R.id.chooseNameButton);

        chooseNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO set name if available
            }
        });
    }

    private String getUserName(){

        //TODO return user name if it has been created before... otherwise return empty string
        return "";
    }
}
