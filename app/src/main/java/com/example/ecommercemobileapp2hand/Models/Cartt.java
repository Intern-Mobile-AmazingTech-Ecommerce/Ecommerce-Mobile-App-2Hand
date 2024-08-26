package com.example.ecommercemobileapp2hand.Models;


public class Cartt{
    private int image;
    private  String name;
    private String size;
    private String color;
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public Cartt(int image, String name, String size, String color, String price) {
        this.image = image;
        this.name = name;
        this.size = size;
        this.color = color;
        this.price = price;
    }
}
