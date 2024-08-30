package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ProductCategory implements Parcelable {
    private int product_category_id;
    private String product_category_name;
    private String product_category_description;
    private String product_category_thumbnail;

    public ProductCategory(int product_category_id, String product_category_name, String product_category_description, String product_category_thumbnail) {
        this.product_category_id = product_category_id;
        this.product_category_name = product_category_name;
        this.product_category_description = product_category_description;
        this.product_category_thumbnail = product_category_thumbnail;
    }
    public ProductCategory(){}

    protected ProductCategory(Parcel in) {
        product_category_id = in.readInt();
        product_category_name = in.readString();
        product_category_description = in.readString();
        product_category_thumbnail = in.readString();
    }

    public static final Creator<ProductCategory> CREATOR = new Creator<ProductCategory>() {
        @Override
        public ProductCategory createFromParcel(Parcel in) {
            return new ProductCategory(in);
        }

        @Override
        public ProductCategory[] newArray(int size) {
            return new ProductCategory[size];
        }
    };

    public int getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(int product_category_id) {
        this.product_category_id = product_category_id;
    }

    public String getProduct_category_name() {
        return product_category_name;
    }

    public void setProduct_category_name(String product_category_name) {
        this.product_category_name = product_category_name;
    }

    public String getProduct_category_description() {
        return product_category_description;
    }

    public void setProduct_category_description(String product_category_description) {
        this.product_category_description = product_category_description;
    }

    public String getProduct_category_thumbnail() {
        return product_category_thumbnail;
    }

    public void setProduct_category_thumbnail(String product_category_thumbnail) {
        this.product_category_thumbnail = product_category_thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(product_category_id);
        dest.writeString(product_category_name);
        dest.writeString(product_category_description);
        dest.writeString(product_category_thumbnail);
    }
}
