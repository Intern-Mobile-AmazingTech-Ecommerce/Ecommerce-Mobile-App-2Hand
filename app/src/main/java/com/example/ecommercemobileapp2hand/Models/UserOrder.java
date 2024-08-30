package com.example.ecommercemobileapp2hand.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class UserOrder {
    private int user_order_id;
    private int user_id;
    private BigDecimal total_price;
    private String order_status_id;
    private LocalDateTime created_at;

    public UserOrder(){}
    public UserOrder(int user_order_id, int user_id, BigDecimal total_price, String order_status_id, LocalDateTime created_at) {
        this.user_order_id = user_order_id;
        this.user_id = user_id;
        this.total_price = total_price;
        this.order_status_id = order_status_id;
        this.created_at = created_at;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(int user_order_id) {
        this.user_order_id = user_order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getTotal_price() {
        return total_price;
    }

    public void setTotal_price(BigDecimal total_price) {
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
