package edu.cpp.preston.saveme;

//notifications from an alert
public class NotificationAlert implements Notification {

    private String personalMessage, sender, time, date;
    private double lat, lon;
    private int index;

    public NotificationAlert(String sender, String personalMessage, double latitude, double longitude, String time, String date) {
        this.personalMessage = personalMessage;
        this.sender = sender;
        this.lat = latitude;
        this.lon = longitude;
        this.time = time;
        this.date = date;
        this.index = -1;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getPersonalMessage() {
        return personalMessage;
    }

    @Override
    public String getTitle() {
        return "Alert!";
    }

    @Override
    public String getDetailedTitle() {
        return "From: " + sender;
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getMessage() {
        return sender + " has sent an alert at " + time + " " + date;
    }

    public double getLat(){
        return lat;
    }

    public double getLon(){
        return lon;
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int i){
        index = i;
    }
}
