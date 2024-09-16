package com.example.ecommercemobileapp2hand.Models;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserOrderProducts implements Serializable {
    private int user_order_id;
    private int product_id;
    private int product_detail_id;
    private int product_detail_size_id;
    private String product_name;
    private String thumbnail;
    private String product_color_name;
    private String size_name;
    private int amount;
    private BigDecimal base_price;
    private BigDecimal sale_price;
    private BigDecimal total_pricepro;
    private boolean isReviewed;


    public UserOrderProducts(){}

    public UserOrderProducts(int user_order_id, int product_id, int product_detail_id, int product_detail_size_id, String product_name, String thumbnail, String product_color_name, String size_name, int amount, BigDecimal base_price, BigDecimal sale_price, BigDecimal total_pricepro, boolean isReviewed) {
        this.user_order_id = user_order_id;
        this.product_id = product_id;
        this.product_detail_id = product_detail_id;
        this.product_detail_size_id = product_detail_size_id;
        this.product_name = product_name;
        this.thumbnail = thumbnail;
        this.product_color_name = product_color_name;
        this.size_name = size_name;
        this.amount = amount;
        this.base_price = base_price;
        this.sale_price = sale_price;
        this.total_pricepro = total_pricepro;
        this.isReviewed = isReviewed;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(int user_order_id) {
        this.user_order_id = user_order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_detail_id() {
        return product_detail_id;
    }

    public void setProduct_detail_id(int product_detail_id) {
        this.product_detail_id = product_detail_id;
    }

    public int getProduct_detail_size_id() {
        return product_detail_size_id;
    }

    public void setProduct_detail_size_id(int product_detail_size_id) {
        this.product_detail_size_id = product_detail_size_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getProduct_color_name() {
        return product_color_name;
    }

    public void setProduct_color_name(String product_color_name) {
        this.product_color_name = product_color_name;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getBase_price() {
        return base_price;
    }

    public void setBase_price(BigDecimal base_price) {
        this.base_price = base_price;
    }

    public BigDecimal getSale_price() {
        return sale_price;
    }

    public void setSale_price(BigDecimal sale_price) {
        this.sale_price = sale_price;
    }

    public BigDecimal getTotal_pricepro() {
        return total_pricepro;
    }

    public void setTotal_pricepro(BigDecimal total_pricepro) {
        this.total_pricepro = total_pricepro;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }
}
