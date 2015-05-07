package edu.cpp.preston.PanicAssist;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

public class StaticMethods {

    String message = "";

    public void sendNotification(String userObjectId, int type){

        if (type == 0){
            message = App.username + " has sent an alert!";
        } else if (type == 1){
            message = "Contact request from " + App.username;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User"); //For sending a push notification
        query.getInBackground(userObjectId, new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    String pushId = user.getString("pushId");

                    // WRONG WAY TO SEND PUSH - INSECURE!
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereEqualTo("installationId", pushId);

                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setMessage(message);
                    push.sendInBackground();
                }
            }
        });
    }
}
