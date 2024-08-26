package com.example.ecommercemobileapp2hand.Models;

import java.time.LocalDateTime;
import java.util.Date;

public class Reviews {
    int reviewID;
    String customerName;
    String avt;
    int ratingPoint;
    String reviewContent;
    LocalDateTime datetime;

    public Reviews(int reviewID, String customerName, String avt, int ratingPoint, String reviewContent) {
        this.reviewID = reviewID;
        this.customerName = customerName;
        this.avt = avt;
        this.ratingPoint = ratingPoint;

        this.reviewContent = reviewContent;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public int getRatingPoint() {
        return ratingPoint;
    }

    public void setRatingPoint(int ratingPoint) {
        this.ratingPoint = ratingPoint;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
