package com.example.ecommercemobileapp2hand.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    int product_id, product_category_id, stock;
    String product_name, description, thumbnail, size, color;
    long base_price, sale_price;

    public Product() {
    }

    public Product(int product_id, int product_category_id, int stock, String product_name, String description, String thumbnail, String size, String color, long base_price, long sale_price) {
        this.product_id = product_id;
        this.product_category_id = product_category_id;
        this.stock = stock;
        this.product_name = product_name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.size = size;
        this.color = color;
        this.base_price = base_price;
        this.sale_price = sale_price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(int product_category_id) {
        this.product_category_id = product_category_id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

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

    public long getBase_price() {
        return base_price;
    }

    public void setBase_price(long base_price) {
        this.base_price = base_price;
    }

    public long getSale_price() {
        return sale_price;
    }

    public void setSale_price(long sale_price) {
        this.sale_price = sale_price;
    }

    public static ArrayList<Product> initProduct()
    {
        ArrayList<Product> lstPro = new ArrayList<>();

        lstPro.add(new Product(1, 2, -1, "Men's Fleece Pullover Hoodie CMMA", "Hoodies Hoodies Hoodies Hoodies", "imgpro1", "null", "null", 100, 0));
        lstPro.add(new Product(2, 2, -1, "Fleece Pullover Skate Hoodie Slina", "Hoodies Hoodies Hoodies Hoodies", "imgpro2", "null", "null", 140, 94));
        lstPro.add(new Product(3, 2, -1, "Fleece Pullover Hoodie", "Hoodies Hoodies Hoodies Hoodies", "imgpro3", "null", "null", 176, 110));
        lstPro.add(new Product(4, 2, -1, "Men's Ice-Dye Pullover Hoodie USA", "Hoodies Hoodies Hoodies Hoodies", "imgpro4", "null", "null", 100, 0));
        lstPro.add(new Product(5, 2, -1, "Men's Skate Pullover Hoodie Rsn", "Hoodies Hoodies Hoodies Hoodies", "imgpro4", "null", "null", 204, 0));
        lstPro.add(new Product(6, 2, -1, "Fleece Pullover Hoodie CMMA Nad", "Hoodies Hoodies Hoodies Hoodies", "imgpro3", "null", "null", 130, 78));

        return lstPro;
    }
}
