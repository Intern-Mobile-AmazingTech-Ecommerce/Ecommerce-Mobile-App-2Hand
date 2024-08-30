package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ProductColor implements Parcelable {
    @JsonProperty("product_color_id")
    private int product_color_id;
    @JsonProperty("product_color_name")
    private String product_color_name;
    public ProductColor(){}
    public ProductColor(int product_color_id, String product_color_name) {
        this.product_color_id = product_color_id;
        this.product_color_name = product_color_name;
    }



    public int getProduct_color_id() {
        return product_color_id;
    }

    public void setProduct_color_id(int product_color_id) {
        this.product_color_id = product_color_id;
    }

    public String getProduct_color_name() {
        return product_color_name;
    }

    public void setProduct_color_name(String product_color_name) {
        this.product_color_name = product_color_name;
    }

    protected ProductColor(Parcel in) {
        product_color_id = in.readInt();
        product_color_name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(product_color_id);
        dest.writeString(product_color_name);
    }
    public static final Creator<ProductColor> CREATOR = new Creator<ProductColor>() {
        @Override
        public ProductColor createFromParcel(Parcel in) {
            return new ProductColor(in);
        }

        @Override
        public ProductColor[] newArray(int size) {
            return new ProductColor[size];
        }
    };

}
