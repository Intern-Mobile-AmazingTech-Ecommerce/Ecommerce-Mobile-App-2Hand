package com.example.ecommercemobileapp2hand.Models;

public class ProductCategory {
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
}
