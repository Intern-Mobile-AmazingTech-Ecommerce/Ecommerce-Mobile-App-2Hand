package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDetailsSize implements Parcelable {
    @JsonProperty("product_details_size_id")
    private int productDetailsSizeID;
    @JsonProperty("product_details_id")
    private int productDetailsID;
    private Size size;
    @JsonProperty("stock")
    private int stock;


    protected ProductDetailsSize(Parcel in) {
        productDetailsSizeID = in.readInt();
        productDetailsID = in.readInt();
        size = in.readParcelable(Size.class.getClassLoader());
        stock = in.readInt();
    }

    public static final Creator<ProductDetailsSize> CREATOR = new Creator<ProductDetailsSize>() {
        @Override
        public ProductDetailsSize createFromParcel(Parcel in) {
            return new ProductDetailsSize(in);
        }

        @Override
        public ProductDetailsSize[] newArray(int size) {
            return new ProductDetailsSize[size];
        }
    };

    @JsonProperty("size_id")
    public void setSizeID(int SizeID){
        if(this.size==null){
            this.size = new Size();
        }
        this.size.setSize_id(SizeID);

    }
    @JsonProperty("size_name")
    public void setSizeName(String sizeName){
        if(this.size==null){
            this.size= new Size();
        }
        this.size.setSize_name(sizeName);
    }
    public ProductDetailsSize(){}

    public ProductDetailsSize(int productDetailsSizeID, int productDetailsID, Size size, int stock) {
        this.productDetailsSizeID = productDetailsSizeID;
        this.productDetailsID = productDetailsID;
        this.size = size;
        this.stock = stock;
    }

    public int getProductDetailsSizeID() {
        return productDetailsSizeID;
    }

    public void setProductDetailsSizeID(int productDetailsSizeID) {
        this.productDetailsSizeID = productDetailsSizeID;
    }

    public int getProductDetailsID() {
        return productDetailsID;
    }

    public void setProductDetailsID(int productDetailsID) {
        this.productDetailsID = productDetailsID;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(productDetailsSizeID);
        dest.writeInt(productDetailsID);
        dest.writeParcelable(size, flags);
        dest.writeInt(stock);
    }
}
