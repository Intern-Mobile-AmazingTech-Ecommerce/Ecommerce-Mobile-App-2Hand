package com.example.ecommercemobileapp2hand.Models;

import java.time.LocalDateTime;
import java.util.Date;

public class UserOrder {
    private String user_order_id;
    private String user_id;
    private double total_price;
    private String order_status_id;
    private LocalDateTime created_at;

    public UserOrder(String user_order_id, String user_id, double total_price, String order_status_id, LocalDateTime created_at) {
        this.user_order_id = user_order_id;
        this.user_id = user_id;
        this.total_price = total_price;
        this.order_status_id = order_status_id;
        this.created_at = created_at;
    }

    public String getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(String user_order_id) {
        this.user_order_id = user_order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(String order_status_id) {
        this.order_status_id = order_status_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
