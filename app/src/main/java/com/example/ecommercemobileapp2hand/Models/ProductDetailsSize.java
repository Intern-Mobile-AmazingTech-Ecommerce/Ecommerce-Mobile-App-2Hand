package com.example.ecommercemobileapp2hand.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDetailsSize {
    @JsonProperty("product_details_size_id")
    private int productDetailsSizeID;
    @JsonProperty("product_details_id")
    private int productDetailsID;
    private Size size;
    @JsonProperty("stock")
    private int stock;

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
}
