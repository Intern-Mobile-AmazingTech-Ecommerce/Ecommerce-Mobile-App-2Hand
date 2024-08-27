package com.example.ecommercemobileapp2hand.Models;

public class OrderStatus {
    private String order_status_id;
    private String order_status_name;

    public OrderStatus(String order_status_id, String order_status_name) {
        this.order_status_id = order_status_id;
        this.order_status_name = order_status_name;
    }

    public String getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(String order_status_id) {
        this.order_status_id = order_status_id;
    }

    public String getOrder_status_name() {
        return order_status_name;
    }

    public void setOrder_status_name(String order_status_name) {
        this.order_status_name = order_status_name;
    }
}
