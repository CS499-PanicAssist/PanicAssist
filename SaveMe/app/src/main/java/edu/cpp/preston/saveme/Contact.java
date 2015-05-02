package edu.cpp.preston.saveme;

public class Contact {
    String name, ID, contactId;
    boolean isNumber, isConfirmed;

    public Contact(String displayName, String usernameOrNumber, boolean isNumber, boolean isConfirmed){
        this.name = displayName;
        this.isNumber = isNumber;
        this.isConfirmed = isConfirmed;

        if (isNumber){
            ID = usernameOrNumber.replaceAll("[^0-9]","");
        } else {
            this.ID = usernameOrNumber;
        }
    }

    public String getdisplayName(){
        return name;
    }

    public String getID(){
        return ID;
    }

    public boolean isNumber(){
        return isNumber;
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

}
