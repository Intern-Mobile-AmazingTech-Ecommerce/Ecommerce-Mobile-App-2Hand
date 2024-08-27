package com.example.ecommercemobileapp2hand.Models;

public class ProductColor {
    private int product_color_id;
    private String product_color_name;

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
}
