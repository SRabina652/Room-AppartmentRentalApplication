package com.example.rentalapplication.model;

public class RatingAndReviewModel {
    Float rating;
    String review;
    String uid;

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RatingAndReviewModel(Float rating, String review, String uid, String name) {
        this.rating = rating;
        this.review = review;
        this.uid = uid;
        this.name = name;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public RatingAndReviewModel() {
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
