package edu.cpp.preston.PanicAssist;

import com.parse.Parse;
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
        userId = sharedSettings.getString("userObjectId", "*");
    }
}