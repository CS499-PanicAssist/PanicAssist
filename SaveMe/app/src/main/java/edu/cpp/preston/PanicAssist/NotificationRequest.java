package edu.cpp.preston.PanicAssist;

//notification from a requests contact
public class NotificationRequest implements Notification {

    private String sender, senderId;

    public NotificationRequest(String sender) {
        this.sender = sender;
    }

    @Override
    public String getTitle() {
        return "Contact Request";
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
        return sender + " is requesting access to alert you in case of an emergency.";
    }

    public void setSenderId(String id) {
        senderId = id;
    }

    public String getSenderId() {
        return senderId;
    }
}