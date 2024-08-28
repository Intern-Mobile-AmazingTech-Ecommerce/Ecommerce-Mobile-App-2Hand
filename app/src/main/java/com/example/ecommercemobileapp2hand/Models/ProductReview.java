package com.example.ecommercemobileapp2hand.Models;

import java.time.LocalDateTime;
import java.util.Date;

public class ProductReview {
    private int product_review_id;
    private String user_id;
    private int product_details_id;
    private String review_content;
    private int rating;
    private LocalDateTime created_at;

    public ProductReview(){}
    public ProductReview(int product_review_id, String user_id, int product_details_id, String review_content, LocalDateTime created_at, int rating) {
        this.product_review_id = product_review_id;
        this.user_id = user_id;
        this.product_details_id = product_details_id;
        this.review_content = review_content;
        this.created_at = created_at;
        this.rating = rating;
    }

    public int getProduct_review_id() {
        return product_review_id;
    }

    public void setProduct_review_id(int product_review_id) {
        this.product_review_id = product_review_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getProduct_details_id() {
        return product_details_id;
    }

    public void setProduct_details_id(int product_details_id) {
        this.product_details_id = product_details_id;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
