package edu.cpp.preston.saveme;

public class Contact {
    private String name, usernameOrNumber, contactId;
    private boolean isNumber, isConfirmed, isSelected;

    public Contact(String displayName, String usernameOrNumber, boolean isNumber, boolean isConfirmed){
        this.name = displayName;
        this.isNumber = isNumber;
        this.isConfirmed = isConfirmed;
        this.isSelected = true;

        if (isNumber){
            this.usernameOrNumber = usernameOrNumber.replaceAll("[^0-9]","");
        } else {
            this.usernameOrNumber = usernameOrNumber;
        }
    }

    public String getdisplayName(){
        return name;
    }

    public String getUsernameOrNumber(){
        return usernameOrNumber;
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

    public void setSelected(boolean is){
        isSelected = is;
    }

    public boolean isSelected(){
        return isSelected;
    }

}
