package edu.cpp.preston.saveme;

public class ContactSaveMe implements Contact{
    private String name, username, contactId;
    private boolean isConfirmed, isSelected;

    public ContactSaveMe(String displayName, String username, boolean isConfirmed){
        this.name = displayName;
        this.isConfirmed = isConfirmed;
        this.isSelected = true;
        this.username = username;
    }

    @Override
    public String getDisplayName(){
        return name;
    }

    @Override
    public String getUsernameOrNumber(){
        return username;
    }

    public void setContactId(String id){
        contactId = id;
    }

    public String getContactId(){
        return contactId;
    }

    public void setIsConfirmed(boolean is){
        isConfirmed = is;
    }

    public boolean isConfirmed(){
        return isConfirmed;
    }

    public void setSelected(boolean is){
        isSelected = is;
    }

    public boolean isSelected(){
        return isSelected;
    }
}