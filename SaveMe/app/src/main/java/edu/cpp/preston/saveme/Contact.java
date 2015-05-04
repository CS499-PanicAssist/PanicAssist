package edu.cpp.preston.saveme;

public interface Contact {

    String getDisplayName();

    String getUsernameOrNumber();

    void setContactId(String id);

    String getContactId();

    void setSelected(boolean is);

    boolean isSelected();
}