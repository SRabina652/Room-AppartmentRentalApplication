package com.example.rentalapplication;

public class ProfileDataHolder {
    String name;
    String UserUID;

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public ProfileDataHolder(String name, String UserUID) {
        this.name = name;
        this.UserUID = UserUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
