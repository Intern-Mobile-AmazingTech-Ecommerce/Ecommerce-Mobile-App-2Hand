package com.example.ecommercemobileapp2hand.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class Product {
    private int product_id;
    private String product_name;

    private String thumbnail;
    private BigDecimal base_price;
    private LocalDateTime created_at;
    private ProductObject productObject;
    private ProductCategory productCategory;

    public Product() {
    }

    public Product(int product_id, String product_name, String thumbnail, BigDecimal base_price, LocalDateTime created_at, ProductObject productObject, ProductCategory productCategory) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.thumbnail = thumbnail;
        this.base_price = base_price;
        this.created_at = created_at;
        this.productObject = productObject;
        this.productCategory = productCategory;
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
}
