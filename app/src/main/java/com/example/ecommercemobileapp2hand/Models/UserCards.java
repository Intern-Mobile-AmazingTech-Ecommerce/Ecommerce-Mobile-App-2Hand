package com.example.ecommercemobileapp2hand.Models;

import java.util.Date;

public class UserCards {
    private int user_cards_id;
    private String user_id;
    private String user_card_number;
    private String user_card_ccv;
    private Date user_card_exp;
    private String user_card_holder_name;

    public UserCards(int user_cards_id, String user_id, String user_card_number, String user_card_ccv, Date user_card_exp, String user_card_holder_name) {
        this.user_cards_id = user_cards_id;
        this.user_id = user_id;
        this.user_card_number = user_card_number;
        this.user_card_ccv = user_card_ccv;
        this.user_card_exp = user_card_exp;
        this.user_card_holder_name = user_card_holder_name;
    }

    public int getUser_cards_id() {
        return user_cards_id;
    }

    public void setUser_cards_id(int user_cards_id) {
        this.user_cards_id = user_cards_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_card_number() {
        return user_card_number;
    }

    public void setUser_card_number(String user_card_number) {
        this.user_card_number = user_card_number;
    }

    public String getUser_card_ccv() {
        return user_card_ccv;
    }

    public void setUser_card_ccv(String user_card_ccv) {
        this.user_card_ccv = user_card_ccv;
    }

    public Date getUser_card_exp() {
        return user_card_exp;
    }

    public void setUser_card_exp(Date user_card_exp) {
        this.user_card_exp = user_card_exp;
    }

    public String getUser_card_holder_name() {
        return user_card_holder_name;
    }

    public void setUser_card_holder_name(String user_card_holder_name) {
        this.user_card_holder_name = user_card_holder_name;
    }
}
