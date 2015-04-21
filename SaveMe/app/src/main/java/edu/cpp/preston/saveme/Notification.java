package edu.cpp.preston.saveme;

/**
 * Created by Preston on 4/15/2015.
 */
public class Notification {
    private String title, message, sender, time, date;
    private int type, ID; //info=1 alert=2 contact request=3
    private double lat, lon;

    public Notification(int type, String sender, String title, String message, double latitude, double longitude, String time, String date) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.sender = sender;
        this.lat = latitude;
        this.lon = longitude;
        this.ID = 777; //TODO fix id
        this.time = time;
        this.date = date;
    }

    public Notification(int type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
        lat = 0.0;
        lon = 0.0;
        this.ID = 777; //TODO fix id
        this.time = "Unknown";
        this.date = "Unknown";
    }

    public Notification(int type, String name, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.sender = name;
        lat = 0.0;
        lon = 0.0;
        this.ID = 777; //TODO fix id
        this.time = "Unknown";
        this.date = "Unknown";
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getID() {
        return ID;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
