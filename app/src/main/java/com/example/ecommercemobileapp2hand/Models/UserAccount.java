package com.example.ecommercemobileapp2hand.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAccount implements Serializable {
    private String userId; // Đổi từ int sang String
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String imgUrl;
    private String gender;
    private String ageRange;

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

    public UserAccount(String userId, String email, String firstName, String lastName, String phoneNumber, String imgUrl, String gender, String ageRange) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.imgUrl = imgUrl;
        this.gender = gender;
        this.ageRange = ageRange;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }
}