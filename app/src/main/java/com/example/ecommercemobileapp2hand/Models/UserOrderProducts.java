package com.example.ecommercemobileapp2hand.Models;

public class UserOrderProducts {
    private int user_order_id;
    private int product_details_size_id;
    private int amount;

    public UserOrderProducts(){}

    public UserOrderProducts(int user_order_id, int product_details_size_id, int amount) {
        this.user_order_id = user_order_id;
        this.product_details_size_id = product_details_size_id;
        this.amount = amount;
    }

    public int getProduct_details_size_id() {
        return product_details_size_id;
    }

    public void setProduct_details_size_id(int product_details_size_id) {
        this.product_details_size_id = product_details_size_id;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(int user_order_id) {
        this.user_order_id = user_order_id;
    }



    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
