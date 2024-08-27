package com.example.ecommercemobileapp2hand.Models;

public class UserOrderProducts {
    private String user_order_id;
    private int product_details_id;
    private int amount;

    public UserOrderProducts(String user_order_id, int amount, int product_details_id) {
        this.user_order_id = user_order_id;
        this.amount = amount;
        this.product_details_id = product_details_id;
    }

    public String getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(String user_order_id) {
        this.user_order_id = user_order_id;
    }

    public int getProduct_details_id() {
        return product_details_id;
    }

    public void setProduct_details_id(int product_details_id) {
        this.product_details_id = product_details_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
