package edu.cpp.preston.PanicAssist;

public class PhoneNumber {
    private String number, name;

    public PhoneNumber(String name, String number){
        this.name = name;
        this.number = number;
    }

    public String getName(){
        return name;
    }

    public String getNumber(){
        return number;
    }

    public String getNumbersOnly(){
        return number.replaceAll("[^0-9]","");
    }
}
