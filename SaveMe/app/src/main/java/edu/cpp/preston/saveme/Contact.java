package edu.cpp.preston.saveme;

public class Contact {
    String name, ID;
    boolean isNumber;

    public Contact(String displayName, String usernameOrNumber, boolean isNumber){
        this.name = displayName;
        this.isNumber = isNumber;

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

}
