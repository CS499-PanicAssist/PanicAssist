package edu.cpp.preston.saveme;

//notification from an admin, such as an information message
public class NotificationInfo implements Notification {

    private String title, message, sender;

    public NotificationInfo(String title, String message) {
        this.title = title;
        this.message = message;
        this.sender = "Admin";
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDetailedTitle() {
        return "Click to view";
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getSenderId(){
        return null;
    }
}
