package com.example.ecommercemobileapp2hand.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    int user_order_id, order_status_id, amount;
    String order_status_name, created_at, shipping_details;

    public Order() {
    }

    public Order(int user_order_id, int order_status_id, int amount, String order_status_name, String created_at, String shipping_details) {
        this.user_order_id = user_order_id;
        this.order_status_id = order_status_id;
        this.amount = amount;
        this.order_status_name = order_status_name;
        this.created_at = created_at;
        this.shipping_details = shipping_details;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(int user_order_id) {
        this.user_order_id = user_order_id;
    }

    public int getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(int order_status_id) {
        this.order_status_id = order_status_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrder_status_name() {
        return order_status_name;
    }

    public void setOrder_status_name(String order_status_name) {
        this.order_status_name = order_status_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getShipping_details() {
        return shipping_details;
    }

    public void setShipping_details(String shipping_details) {
        this.shipping_details = shipping_details;
    }

    public static ArrayList<Order> initOrder()
    {
        ArrayList<Order> lstorders = new ArrayList<>();
        lstorders.add(new Order(456765, 3, 4, "Shipped", "28 May", "2715 Ash Dr. San Jose, South Dakota 83475 \n121-224-7890"));
        lstorders.add(new Order(454569, 2, 2, "Order Confirmed", "29 May", "2715 Ash Dr. San Jose, South Dakota 83475 \n121-224-7890"));
        lstorders.add(new Order(454809, 1, 1, "Order Placed", "27 May", "2715 Ash Dr. San Jose, South Dakota 83475 \n121-224-7890"));

        return lstorders;
    }
}
