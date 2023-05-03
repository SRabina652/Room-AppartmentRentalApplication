package com.example.rentalapplication.model;

public class ProfileDataHolder {
    String name;
    String userUID;

    public ProfileDataHolder() {
    }

    public ProfileDataHolder(String name, String userUID) {
        this.name = name;
        this.userUID = userUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String image) {
        this.userUID = userUID;
    }
}
