package com.example.ecommercemobileapp2hand.Models.FakeModels;

public class Card {
    private String cardNumber;
    private String ccv;
    private String expirationDate;
    private String cardHolderName;


    public Card(String cardNumber, String ccv, String expirationDate, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.ccv = ccv;
        this.expirationDate = expirationDate;
        this.cardHolderName = cardHolderName;

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCcv() {
        return ccv;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }
}
