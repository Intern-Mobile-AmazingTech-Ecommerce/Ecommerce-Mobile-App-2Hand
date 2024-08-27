package com.example.ecommercemobileapp2hand.Models;

public class Cart {
    private int image;
    private String name;
    private String size;
    private String color;
    private String price;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cart(int image, String name, String size, String color, String price) {
        this.image = image;
        this.name = name;
        this.size = size;
        this.color = color;
        this.price = price;
    }
}
