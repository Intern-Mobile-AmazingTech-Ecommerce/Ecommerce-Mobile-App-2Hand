package com.example.ecommercemobileapp2hand.Models;

public class Bag {
    private int bag_id;
    private String user_id;
    private int product_details_id;

    public int getBag_id() {
        return bag_id;
    }

    public void setBag_id(int bag_id) {
        this.bag_id = bag_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getProduct_details_id() {
        return product_details_id;
    }

    public void setProduct_details_id(int product_details_id) {
        this.product_details_id = product_details_id;
    }

    public Bag(int bag_id, String user_id, int product_details_id) {
        this.bag_id = bag_id;
        this.user_id = user_id;
        this.product_details_id = product_details_id;
    }
    public Bag(){}
}
