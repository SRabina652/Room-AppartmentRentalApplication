package com.example.rentalapplication.model;

public class RatingAndReviewModel {
    Float rating;
    String review;

    public RatingAndReviewModel(Float rating, String review) {
        this.rating = rating;
        this.review = review;
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
