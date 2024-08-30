package com.example.ecommercemobileapp2hand.Models;

public class UserAddress {

    private int user_address_id;
    private int user_id;
    private String user_address_street;
    private String user_address_city;
    private String user_address_state;
    private String user_address_zipcode;

    public UserAddress() {
    }

    public UserAddress(int user_address_id, int user_id, String user_address_street, String user_address_city, String user_address_state, String user_address_zipcode) {
        this.user_address_id = user_address_id;
        this.user_id = user_id;
        this.user_address_street = user_address_street;
        this.user_address_city = user_address_city;
        this.user_address_state = user_address_state;
        this.user_address_zipcode = user_address_zipcode;
    }

    public int getUser_address_id() {
        return user_address_id;
    }

    public void setUser_address_id(int user_address_id) {
        this.user_address_id = user_address_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_address_street() {
        return user_address_street;
    }

    public void setUser_address_street(String user_address_street) {
        this.user_address_street = user_address_street;
    }

    public String getUser_address_city() {
        return user_address_city;
    }

    public void setUser_address_city(String user_address_city) {
        this.user_address_city = user_address_city;
    }

    public String getUser_address_state() {
        return user_address_state;
    }

    public void setUser_address_state(String user_address_state) {
        this.user_address_state = user_address_state;
    }

    public String getUser_address_zipcode() {
        return user_address_zipcode;
    }

    public void setUser_address_zipcode(String user_address_zipcode) {
        this.user_address_zipcode = user_address_zipcode;
    }
}
