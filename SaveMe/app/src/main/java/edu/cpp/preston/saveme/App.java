package edu.cpp.preston.saveme;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this); // Enable Local Datastore.
        Parse.initialize(this, "v3QSSbAhpftwrkDaf9m81lccjsE2VZnFrAFwWxiP", "Z7NpBHFHRJFH536slLBtR9jYdujQwE8XkF8XQFdm");
    }
}