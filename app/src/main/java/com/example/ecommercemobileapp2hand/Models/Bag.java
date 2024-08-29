package com.example.ecommercemobileapp2hand.Models;

public class Bag {
    private int bag_id;
    private int user_id;
    private int product_details_size_id;
    private int amount;
    public int getBag_id() {
        return bag_id;
    }

    public void setBag_id(int bag_id) {
        this.bag_id = bag_id;
    }


    public Bag(int bag_id, int product_details_size_id, int user_id,int amount) {
        this.bag_id = bag_id;
        this.product_details_size_id = product_details_size_id;
        this.user_id = user_id;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProduct_details_size_id() {
        return product_details_size_id;
    }

    public void setProduct_details_size_id(int product_details_size_id) {
        this.product_details_size_id = product_details_size_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public Bag(){}
}
