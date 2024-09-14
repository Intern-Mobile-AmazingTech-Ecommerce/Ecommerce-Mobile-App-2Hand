package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Product implements Parcelable {

    private int product_id;
    private String product_name;
    private String thumbnail;
    private BigDecimal base_price;
    private int isFreeship;
    private LocalDateTime created_at;
    private int coupon_id;
    private ProductObject productObject;
    private ProductCategory productCategory;
    @JsonProperty ("product_details_array")
    private ArrayList<ProductDetails> productDetailsArrayList;
    private BigDecimal sold;

    public BigDecimal getSold() {
        return sold;
    }

    public void setSold(BigDecimal sold) {
        this.sold = sold;
    }

    public Product() {
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getIsFreeship() {
        return isFreeship;
    }

    public void setIsFreeship(int isFreeship) {
        this.isFreeship = isFreeship;
    }

    public Product(int product_id, String product_name, String thumbnail, BigDecimal base_price, int isFreeship, LocalDateTime created_at, int coupon_id, ProductObject productObject, ProductCategory productCategory, ArrayList<ProductDetails> productDetailsArrayList, BigDecimal sold) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.thumbnail = thumbnail;
        this.base_price = base_price;
        this.isFreeship = isFreeship;
        this.created_at = created_at;
        this.coupon_id = coupon_id;
        this.productObject = productObject;
        this.productCategory = productCategory;
        this.productDetailsArrayList = productDetailsArrayList;
        this.sold = sold;
    }

    public ArrayList<ProductDetails> getProductDetailsArrayList() {
        return productDetailsArrayList;
    }

    public void setProductDetailsArrayList(ArrayList<ProductDetails> productDetailsArrayList) {
        this.productDetailsArrayList = productDetailsArrayList;
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

    public ProductObject getProductObject() {
        return productObject;
    }

    public void setProductObject(ProductObject productObject) {
        this.productObject = productObject;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BigDecimal getBase_price() {
        return base_price;
    }

    public void setBase_price(BigDecimal base_price) {
        this.base_price = base_price;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }


    //Parcelable
    protected Product(Parcel in) {
        product_id = in.readInt();
        product_name = in.readString();
        thumbnail = in.readString();
        base_price = new BigDecimal(in.readString());
        isFreeship = in.readInt();
        created_at = LocalDateTime.parse(in.readString());
        coupon_id = in.readInt();
        productObject = in.readParcelable(ProductObject.class.getClassLoader());
        productCategory = in.readParcelable(ProductCategory.class.getClassLoader());
        productDetailsArrayList = in.createTypedArrayList(ProductDetails.CREATOR);
        sold = new BigDecimal(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(product_id);
        dest.writeString(product_name);
        dest.writeString(thumbnail);
        dest.writeString(base_price.toString());
        dest.writeInt(isFreeship);
        // Chuyển đổi LocalDateTime thành chuỗi
        dest.writeString(created_at.toString());
        dest.writeInt(coupon_id);
        dest.writeParcelable(productObject, flags);
        dest.writeParcelable(productCategory, flags);
        dest.writeTypedList(productDetailsArrayList);
        dest.writeString(sold.toString());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}
