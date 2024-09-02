package com.example.ecommercemobileapp2hand.Models;

import net.sourceforge.jtds.jdbc.DateTime;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Notifications implements Serializable {
    private int notifications_id;
    private String notifications_content;
    private String created_at;
    private boolean viewed;
    public Notifications(){

    }

    public Notifications(int notifications_id, String notifications_content, String created_at, boolean viewed) {
        this.notifications_id = notifications_id;
        this.notifications_content = notifications_content;
        this.created_at = created_at;
        this.viewed = viewed;
    }

    public int getNotifications_id() {
        return notifications_id;
    }

    public void setNotifications_id(int notifications_id) {
        this.notifications_id = notifications_id;
    }

    public String getNotifications_content() {
        return notifications_content;
    }

    public void setNotifications_content(String notifications_content) {
        this.notifications_content = notifications_content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
