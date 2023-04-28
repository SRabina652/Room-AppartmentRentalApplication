package com.example.rentalapplication.model;

public class chatModel {
    String name;
    String image;
    String userUid;
    String status;

    public chatModel() {
    }

    public chatModel(String name, String image, String userUid, String status) {
        this.name = name;
        this.image = image;
        this.userUid = userUid;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
