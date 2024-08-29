package com.example.ecommercemobileapp2hand.Models;

public class ProductDetailsSize {
    private int productDetailsSizeID;
    private int productDetailsID;
    private int sizeID;
    private int stock;
    public ProductDetailsSize(){}
    public ProductDetailsSize(int productDetailsSizeID, int productDetailsID, int sizeID, int stock) {
        this.productDetailsSizeID = productDetailsSizeID;
        this.productDetailsID = productDetailsID;
        this.sizeID = sizeID;
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

    public int getSizeID() {
        return sizeID;
    }

    public void setSizeID(int sizeID) {
        this.sizeID = sizeID;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
