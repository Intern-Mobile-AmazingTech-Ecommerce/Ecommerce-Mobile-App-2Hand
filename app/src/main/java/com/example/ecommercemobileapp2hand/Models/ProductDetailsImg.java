package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDetailsImg implements Parcelable {
    @JsonProperty("product_img_id")
    private int product_img_id;
    @JsonProperty("product_details_id")
    private int product_details_id;
    @JsonProperty("img_url")
    private String img_url;

    public ProductDetailsImg(){}
    public ProductDetailsImg(int product_img_id, int product_details_id, String img_url) {
        this.product_img_id = product_img_id;
        this.product_details_id = product_details_id;
        this.img_url = img_url;
    }

    protected ProductDetailsImg(Parcel in) {
        product_img_id = in.readInt();
        product_details_id = in.readInt();
        img_url = in.readString();
    }

    public static final Creator<ProductDetailsImg> CREATOR = new Creator<ProductDetailsImg>() {
        @Override
        public ProductDetailsImg createFromParcel(Parcel in) {
            return new ProductDetailsImg(in);
        }

        @Override
        public ProductDetailsImg[] newArray(int size) {
            return new ProductDetailsImg[size];
        }
    };

    public int getProduct_img_id(int anInt) {
        return product_img_id;
    }

    public void setProduct_img_id(int product_img_id) {
        this.product_img_id = product_img_id;
    }

    public int getProduct_details_id(int anInt) {
        return product_details_id;
    }

    public void setProduct_details_id(int product_details_id) {
        this.product_details_id = product_details_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(product_img_id);
        dest.writeInt(product_details_id);
        dest.writeString(img_url);
    }
}
