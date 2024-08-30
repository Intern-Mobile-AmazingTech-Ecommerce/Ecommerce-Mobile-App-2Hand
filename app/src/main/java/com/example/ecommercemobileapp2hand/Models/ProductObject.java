package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProductObject implements Parcelable {
    private int product_object_id;
    private String object_name;

    public ProductObject(){}
    public ProductObject(int product_object_id, String object_name) {
        this.product_object_id = product_object_id;
        this.object_name = object_name;
    }

    protected ProductObject(Parcel in) {
        product_object_id = in.readInt();
        object_name = in.readString();
    }

    public static final Creator<ProductObject> CREATOR = new Creator<ProductObject>() {
        @Override
        public ProductObject createFromParcel(Parcel in) {
            return new ProductObject(in);
        }

        @Override
        public ProductObject[] newArray(int size) {
            return new ProductObject[size];
        }
    };

    public int getProduct_object_id() {
        return product_object_id;
    }

    public void setProduct_object_id(int product_object_id) {
        this.product_object_id = product_object_id;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(product_object_id);
        dest.writeString(object_name);
    }
}
