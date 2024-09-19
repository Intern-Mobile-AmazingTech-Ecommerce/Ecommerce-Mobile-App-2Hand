package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bag implements Parcelable {
    private int bag_id;
    private String user_id;
    private int product_details_size_id;
    private int product_details_id;
    private int product_id;
    private String product_name;
    private BigDecimal basePrice;
    private BigDecimal salePrice;
    private String size;
    private int amount;
    private String color;
    private String image;
    private Product product;

    public Bag(int bag_id, String user_id, int product_details_size_id, int product_details_id, int product_id, String product_name, BigDecimal basePrice, BigDecimal salePrice, String size, int amount, String color, String image, Product product) {
        this.bag_id = bag_id;
        this.user_id = user_id;
        this.product_details_size_id = product_details_size_id;
        this.product_details_id = product_details_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.basePrice = basePrice;
        this.salePrice = salePrice;
        this.size = size;
        this.amount = amount;
        this.color = color;
        this.image = image;
        this.product = product;
    }

    protected Bag(Parcel in) {
        bag_id = in.readInt();
        user_id = in.readString();
        product_details_size_id = in.readInt();
        product_details_id = in.readInt();
        product_id = in.readInt();
        product_name = in.readString();
        size = in.readString();
        amount = in.readInt();
        color = in.readString();
        image = in.readString();
        basePrice= new BigDecimal(in.readString());
        salePrice= new BigDecimal(in.readString());
        product = in.readParcelable(Product.class.getClassLoader());
    }

    public static final Creator<Bag> CREATOR = new Creator<Bag>() {
        @Override
        public Bag createFromParcel(Parcel in) {
            return new Bag(in);
        }

        @Override
        public Bag[] newArray(int size) {
            return new Bag[size];
        }
    };

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public int getBag_id() {
        return bag_id;
    }

    public Bag() {
    }




    public void setBag_id(int bag_id) {
        this.bag_id = bag_id;
    }

    public int getProduct_details_size_id() {
        return product_details_size_id;
    }

    public void setProduct_details_size_id(int product_details_size_id) {
        this.product_details_size_id = product_details_size_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProduct_details_id() {
        return product_details_id;
    }

    public void setProduct_details_id(int product_details_id) {
        this.product_details_id = product_details_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(bag_id);
        parcel.writeString(user_id);
        parcel.writeInt(product_details_size_id);
        parcel.writeInt(product_details_id);
        parcel.writeInt(product_id);
        parcel.writeString(product_name);
        parcel.writeString(size);
        parcel.writeInt(amount);
        parcel.writeString(color);
        parcel.writeString(image);
        parcel.writeString(basePrice.toString());
        parcel.writeString(salePrice.toString());
        parcel.writeParcelable(product, i);
    }
}
