package com.example.ecommercemobileapp2hand.Models;

public class UserAddress {
    private int user_address_id;
    private String user_id;
    private String user_address_content;

    public UserAddress(){}
    public UserAddress(int user_address_id, String user_id, String user_address_content) {
        this.user_address_id = user_address_id;
        this.user_id = user_id;
        this.user_address_content = user_address_content;
    }

    public int getUser_address_id() {
        return user_address_id;
    }

    public void setUser_address_id(int user_address_id) {
        this.user_address_id = user_address_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_address_content() {
        return user_address_content;
    }

    public void setUser_address_content(String user_address_content) {
        this.user_address_content = user_address_content;
    }
}
