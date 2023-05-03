package com.example.rentalapplication.model;

public class Message {
    String Message;
    String SenderId;
    long Timestamp;
    String currentTime;

    public Message(String message, String senderId, long timestamp, String currentTime) {
        Message = message;
        SenderId = senderId;
        Timestamp = timestamp;
        this.currentTime = currentTime;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public Message() {
    }
}
