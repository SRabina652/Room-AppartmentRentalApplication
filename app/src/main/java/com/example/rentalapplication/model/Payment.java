package com.example.rentalapplication.model;

public class Payment {
    String paidUserUID;
    String price;

    public Payment(String paidUserUID, String price) {
        this.paidUserUID = paidUserUID;
        this.price = price;
    }

    public String getPaidUserUID() {
        return paidUserUID;
    }

    public void setPaidUserUID(String paidUserUID) {
        this.paidUserUID = paidUserUID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
