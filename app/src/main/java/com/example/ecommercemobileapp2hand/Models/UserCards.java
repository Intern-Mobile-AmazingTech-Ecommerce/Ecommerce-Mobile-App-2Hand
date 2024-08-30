package com.example.ecommercemobileapp2hand.Models;

import java.sql.Date;


public class UserCards {
    private int userCardsId;
    private int userId;
    private String cardNumber;
    private String ccv;
    private Date expirationDate;
    private String cardHolderName;

    public UserCards(int userCardsId, int userId, String cardNumber, String ccv, Date expirationDate, String cardHolderName) {
        this.userCardsId = userCardsId;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.ccv = ccv;
        this.expirationDate = expirationDate;
        this.cardHolderName = cardHolderName;
    }

    public int getUserCardsId() {
        return userCardsId;
    }

    public void setUserCardsId(int userCardsId) {
        this.userCardsId = userCardsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
}
