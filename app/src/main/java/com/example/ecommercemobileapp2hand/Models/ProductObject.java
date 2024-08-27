package com.example.ecommercemobileapp2hand.Models;

public class ProductObject {
    private int product_object_id;
    private String object_name;

    public ProductObject(int product_object_id, String object_name) {
        this.product_object_id = product_object_id;
        this.object_name = object_name;
    }

    public int getProduct_object_id() {
        return product_object_id;
    }

    public void setProduct_object_id(int product_object_id) {
        this.product_object_id = product_object_id;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

}
