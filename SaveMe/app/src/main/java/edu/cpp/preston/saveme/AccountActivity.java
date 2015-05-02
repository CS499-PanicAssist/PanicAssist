package edu.cpp.preston.saveme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class AccountActivity extends ActionBarActivity {

    static SharedPreferences sharedPrefSettings;
    static SharedPreferences.Editor editor;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        sharedPrefSettings = this.getSharedPreferences(getString(R.string.preference_file_general_settings_key), Context.MODE_PRIVATE);
        editor = sharedPrefSettings.edit();

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        nameEditText.setText(getUserName());
        Button chooseNameButton = (Button) findViewById(R.id.chooseNameButton);

        chooseNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String enteredText = nameEditText.getText().toString();
                final String userEmail = UserEmailFetcher.getEmail(getApplicationContext());

                if (!enteredText.equalsIgnoreCase(getUserName())) { //make sure user isnt sending username they already have
                    if (enteredText.length() < 5) { //username not long enough
                        Toast.makeText(getActivity(), "Must be at least 5 characters long", Toast.LENGTH_SHORT).show();
                    } else { // username length is long enough
                        progress = new ProgressDialog(getActivity());
                        progress.setTitle("Setting username");
                        progress.setMessage("Please wait...");
                        progress.show();

                        if (!sharedPrefSettings.contains("userEmail")) { // if username isnt locally stored, store it
                            if (userEmail == null) {
                                Toast.makeText(getActivity(), "Error accessing user email address", Toast.LENGTH_SHORT).show();
                            } else {
                                editor.putString("userEmail", userEmail);
                                editor.commit();
                            }
                        }

                        if (sharedPrefSettings.contains("userEmail")) { // email should be stored by now
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                            query.whereEqualTo("username", enteredText);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> queryUsernamesList, ParseException e) {
                                    if (e == null) {
                                        if (queryUsernamesList.size() > 0 && queryUsernamesList.get(0).getString("email").equalsIgnoreCase(userEmail)){
                                            editor.putString("username", enteredText);
                                            editor.commit();
                                        } else if (queryUsernamesList.size() == 0) { //username is not taken so add it
                                            changeUserName(userEmail, enteredText);
                                        } else { //username taken
                                            Toast.makeText(getApplicationContext(), "Username taken, please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error, please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                    progress.dismiss();
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Username set!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeUserName(final String userEmail, final String newUsername){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

        query.whereEqualTo("email", userEmail);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queryEmailList, ParseException e) {
                if (e == null) {
                    if (queryEmailList.size() == 0) { //user is not made yet in database
                        final ParseObject user = new ParseObject("User"); //make new user
                        user.put("username", newUsername);
                        user.put("email", userEmail);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                editor.putString("userObjectId", user.getObjectId()); //this users id is now saved, and is permanent
                                editor.commit();
                            }
                        }); ;

                        editor.putString("username", newUsername);
                        editor.commit();
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Username set!", Toast.LENGTH_SHORT).show();
                    } else { //edit existing  user
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

                        query.getInBackground(queryEmailList.get(0).getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject gameScore, ParseException e) {
                                if (e == null) {
                                    gameScore.put("username", newUsername); //update username
                                    gameScore.saveInBackground();
                                    editor.putString("username", newUsername);
                                    editor.commit();
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Username set!", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Error, please try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error, please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private String getUserName() {
        return sharedPrefSettings.getString("username", "");
    }

    private Activity getActivity() {
        return this;
    }
}
