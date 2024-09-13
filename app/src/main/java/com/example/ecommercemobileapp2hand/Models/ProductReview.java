package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ProductReview implements Parcelable {
    @JsonProperty("product_review_id")
    private int product_review_id;
    @JsonProperty("user_id")
    private String user_id;
    @JsonProperty("product_details_id")
    private int product_details_id;
    @JsonProperty("review_content")
    private String review_content;
    @JsonProperty("rating")
    private int rating;
    @JsonProperty("created_at")
    private LocalDateTime created_at;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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
    protected ProductReview(Parcel in) {
        product_review_id = in.readInt();
        user_id = in.readString();
        product_details_id = in.readInt();
        review_content = in.readString();
        rating = in.readInt();

        // Deserialize LocalDateTime from String (ISO format)
        String createdAtString = in.readString();
        if (createdAtString != null) {
            created_at = LocalDateTime.parse(createdAtString, DateTimeFormatter.ISO_DATE_TIME);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(product_review_id);
        dest.writeString(user_id);
        dest.writeInt(product_details_id);
        dest.writeString(review_content);
        dest.writeInt(rating);

        // Serialize LocalDateTime as String (ISO format)
        dest.writeString(created_at != null ? created_at.toString() : null);
    }
    public static final Creator<ProductReview> CREATOR = new Creator<ProductReview>() {
        @Override
        public ProductReview createFromParcel(Parcel in) {
            return new ProductReview(in);
        }

        @Override
        public ProductReview[] newArray(int size) {
            return new ProductReview[size];
        }
    };
}
