package com.example.ecommercemobileapp2hand.Models;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserOrder implements Serializable {
    private int user_order_id;
    private String user_id;
    private int user_address_id;
    private BigDecimal total_price;
    private int order_status_id;
    private String created_at;
    private BigDecimal total_sale;

    public UserOrder() {
    }

    public UserOrder(int user_order_id,String user_id, int user_address_id, BigDecimal total_price, int order_status_id, String created_at, BigDecimal total_sale) {
        this.user_order_id = user_order_id;
        this.user_id=user_id;
        this.user_address_id = user_address_id;
        this.total_price = total_price;
        this.order_status_id = order_status_id;
        this.created_at = created_at;
        this.total_sale = total_sale;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(int user_order_id) {
        this.user_order_id = user_order_id;
    }

    public int getUser_address_id() {
        return user_address_id;
    }

    public void setUser_address_id(int user_address_id) {
        this.user_address_id = user_address_id;
    }

    public BigDecimal getTotal_price() {
        return total_price;
    }

    public void setTotal_price(BigDecimal total_price) {
        this.total_price = total_price;
    }

    public int getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(int order_status_id) {
        this.order_status_id = order_status_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public BigDecimal getTotal_sale() {
        return total_sale;
    }

    public void setTotal_sale(BigDecimal total_sale) {
        this.total_sale = total_sale;
    }
}
