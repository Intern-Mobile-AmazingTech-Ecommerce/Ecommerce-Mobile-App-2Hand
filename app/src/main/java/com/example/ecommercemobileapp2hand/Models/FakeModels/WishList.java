package com.example.ecommercemobileapp2hand.Models.FakeModels;

public class WishList {

    public WishList(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    String name;


    int quantity;

    public WishList() {
    }

}
