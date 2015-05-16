package edu.cpp.preston.PanicAssist;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {

    protected static String username;
    protected static String userId;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this); // Enable Local Datastore.
        Parse.initialize(this, "v3QSSbAhpftwrkDaf9m81lccjsE2VZnFrAFwWxiP", "Z7NpBHFHRJFH536slLBtR9jYdujQwE8XkF8XQFdm");

        SharedPreferences sharedSettings = this.getSharedPreferences(getString(R.string.preference_file_general_settings_key), Context.MODE_PRIVATE);
        username = sharedSettings.getString("username", "*");
        userId = sharedSettings.getString("userObjectId", "Unknown");

        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (!userId.equalsIgnoreCase("Unknown")) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

                    // Retrieve the object by id
                    query.getInBackground(userId, new GetCallback<ParseObject>() {
                        public void done(ParseObject user, ParseException e) {
                            if (e == null) {
                                user.put("pushId", ParseInstallation.getCurrentInstallation().getInstallationId());
                                user.saveInBackground();
                            }
                        }
                    });
                }
            }
        });
    }

}