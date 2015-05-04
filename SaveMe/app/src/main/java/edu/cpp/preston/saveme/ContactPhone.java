package edu.cpp.preston.saveme;

public class ContactPhone implements Contact {
    private String name, number, contactId;
    private boolean isSelected;

    public ContactPhone(String displayName, String number){
        this.name = displayName;
        this.isSelected = true;
        this.number = number.replaceAll("[^0-9]","");
    }

    @Override
    public String getDisplayName(){
        return name;
    }

    @Override
    public String getUsernameOrNumber(){
        return number;
    }

    @Override
    public void setContactId(String id){
        contactId = id;
    }

    @Override
    public String getContactId(){
        return contactId;
    }

    @Override
    public void setSelected(boolean is){
        isSelected = is;
    }

    @Override
    public boolean isSelected(){
        return isSelected;
    }

}