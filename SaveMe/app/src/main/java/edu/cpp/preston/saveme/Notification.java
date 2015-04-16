package edu.cpp.preston.saveme;

/**
 * Created by Preston on 4/15/2015.
 */
public class Notification {
    private String title, message, sender;
    private int type; //info=1 alert=2 contact request=3
    private double lat, lon;

    public Notification(int type, String sender, String title, String message, double latitude, double longitude){
        this.type = type;
        this.title = title;
        this.message = message;
        this.sender = sender;
        this.lat = latitude;
        this.lon = longitude;
    }

    public Notification(int type, String title, String message){
        this.type = type;
        this.title = title;
        this.message = message;
        lat = 0.0;
        lon = 0.0;
    }

    public int getType(){
        return type;
    }

    public String getTitle(){
        return title;
    }

    public String getSender(){
        return sender;
    }

    public String getMessage(){
        return message;
    }

    public double getLat(){
        return lat;
    }

    public double getLon(){
        return lon;
    }
}
