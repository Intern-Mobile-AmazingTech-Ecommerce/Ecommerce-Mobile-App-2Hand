package com.example.ecommercemobileapp2hand.Models;

public class UserAccount {
    private String user_id;
    private String username;
    private String password;
    private String gender;
    private String email;
    private String phone_number;
    private String first_name;
    private String last_name;

    public UserAccount(){}
    public UserAccount(String user_id, String username, String password, String gender, String email, String phone_number, String first_name, String last_name) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
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
