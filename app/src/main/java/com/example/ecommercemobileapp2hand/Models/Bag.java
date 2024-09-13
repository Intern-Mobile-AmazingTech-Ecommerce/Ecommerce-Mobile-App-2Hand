package com.example.ecommercemobileapp2hand.Models;

import java.io.Serializable;

public class Bag implements Serializable {
    private int bag_id;
    private String user_id;
    private int product_details_size_id;
    private int amount;
    public int getBag_id() {
        return bag_id;
    }

    public Bag() {
    }

    public Bag(int bag_id,String user_id, int product_details_size_id, int amount) {
        this.bag_id = bag_id;
        this.user_id=user_id;
        this.product_details_size_id = product_details_size_id;
        this.amount = amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setBag_id(int bag_id) {
        this.bag_id = bag_id;
    }

    public int getProduct_details_size_id() {
        return product_details_size_id;
    }

    public void setProduct_details_size_id(int product_details_size_id) {
        this.product_details_size_id = product_details_size_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
