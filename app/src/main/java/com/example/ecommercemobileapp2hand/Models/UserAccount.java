package com.example.ecommercemobileapp2hand.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAccount implements Serializable {
    private int user_id;
    private String username;
    private String password;
    private String gender;
    private String email;
    private String phone_number;
    private String first_name;
    private String last_name;
    private String img_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    private Bag bag_user;
    private ArrayList<Wishlist> lstWL;
    private ArrayList<Notifications> lstNoti;
    private ArrayList<UserCards> lstCard;
    private ArrayList<UserOrder> lstOrder;
    private ArrayList<UserAddress> lstAddress;

    public Bag getBag_user() {
        return bag_user;
    }

    public void setBag_user(Bag bag_user) {
        this.bag_user = bag_user;
    }

    public ArrayList<Wishlist> getLstWL() {
        return lstWL;
    }

    public void setLstWL(ArrayList<Wishlist> lstWL) {
        this.lstWL = lstWL;
    }

    public ArrayList<Notifications> getLstNoti() {
        return lstNoti;
    }

    public void setLstNoti(ArrayList<Notifications> lstNoti) {
        this.lstNoti = lstNoti;
    }

    public ArrayList<UserCards> getLstCard() {
        return lstCard;
    }

    public void setLstCard(ArrayList<UserCards> lstCard) {
        this.lstCard = lstCard;
    }

    public ArrayList<UserOrder> getLstOrder() {
        return lstOrder;
    }

    public void setLstOrder(ArrayList<UserOrder> lstOrder) {
        this.lstOrder = lstOrder;
    }

    public ArrayList<UserAddress> getLstAddress() {
        return lstAddress;
    }

    public void setLstAddress(ArrayList<UserAddress> lstAddress) {
        this.lstAddress = lstAddress;
    }

    public UserAccount(){}

    public UserAccount(int user_id, String username, String password, String gender, String email, String phone_number, String first_name, String last_name, String img_url) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.img_url = img_url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
