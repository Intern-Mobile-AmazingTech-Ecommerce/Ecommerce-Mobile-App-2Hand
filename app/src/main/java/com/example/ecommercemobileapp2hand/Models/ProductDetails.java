package com.example.ecommercemobileapp2hand.Models;

import java.math.BigDecimal;

public class ProductDetails {
    private int product_details_id;
    private int product_id;
    private int product_color_id;
    private String description;
    private BigDecimal sale_price;
    public ProductDetails(){}


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

    public int getProduct_color_id() {
        return product_color_id;
    }

    public void setProduct_color_id(int product_color_id) {
        this.product_color_id = product_color_id;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSale_price() {
        return sale_price;
    }

    public void setSale_price(BigDecimal sale_price) {
        this.sale_price = sale_price;
    }

    public ProductDetails(int product_details_id, int product_id, int product_color_id, String description, BigDecimal sale_price) {
        this.product_details_id = product_details_id;
        this.product_id = product_id;
        this.product_color_id = product_color_id;
        this.description = description;
        this.sale_price = sale_price;
    }

}
