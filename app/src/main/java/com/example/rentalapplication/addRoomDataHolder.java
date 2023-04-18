package com.example.rentalapplication;

public class addRoomDataHolder {
    String price;
    String peoples;
    String requirement;
    String facilities;
    String landmark;

    public addRoomDataHolder() {
    }

    String Location;
    String roomImg;

    public String getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(String checkedItems) {
        this.checkedItems = checkedItems;
    }

    String checkedItems,Rooms;

    String student, Family, workingWomen, workingMen;

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getFamily() {
        return Family;
    }

    public void setFamily(String family) {
        Family = family;
    }

    public String getWorkingWomen() {
        return workingWomen;
    }

    public void setWorkingWomen(String workingWomen) {
        this.workingWomen = workingWomen;
    }

    public String getWorkingMen() {
        return workingMen;
    }

    public void setWorkingMen(String workingMen) {
        this.workingMen = workingMen;
    }

    public String getRooms() {
        return Rooms;
    }

    public void setRooms(String rooms) {
        Rooms = rooms;
    }

    public addRoomDataHolder(String Rooms, String checkedItems, String price, String peoples, String requirement, String facilities, String landmark, String location, String roomImg) {
        this.Rooms=Rooms;
        this.checkedItems= checkedItems;
        this.price = price;
        this.peoples = peoples;
        this.requirement = requirement;
        this.facilities = facilities;
        this.landmark = landmark;
        Location = location;
        this.roomImg = roomImg;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
    }
}
