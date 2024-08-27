package com.example.ecommercemobileapp2hand.Models.FakeModels;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderStatus implements Serializable {
    int order_status_id;
    String order_status_name;
    boolean isCompleted;

    public OrderStatus() {
    }

    public OrderStatus(int order_status_id, String order_status_name, boolean isCompleted) {
        this.order_status_id = order_status_id;
        this.order_status_name = order_status_name;
        this.isCompleted = isCompleted;
    }

    public int getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(int order_status_id) {
        this.order_status_id = order_status_id;
    }

    public String getOrder_status_name() {
        return order_status_name;
    }

    public void setOrder_status_name(String order_status_name) {
        this.order_status_name = order_status_name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public static ArrayList<OrderStatus> initOrderStatus()
    {
        ArrayList<OrderStatus> lst = new ArrayList<>();
        lst.add(new OrderStatus(1, "Order Placed", false));
        lst.add(new OrderStatus(2, "Order Confirmed", false));
        lst.add(new OrderStatus(3, "Shipped", false));
        lst.add(new OrderStatus(4, "Delivered", false));
        lst.add(new OrderStatus(5, "Returned", false));
        lst.add(new OrderStatus(6, "Canceled", false));

        return lst;
    }
}
