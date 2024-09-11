package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Notifications;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class NotificationsHandler {
    public static ArrayList<Notifications> getNotifications(String userId) {
        ArrayList<Notifications> notificationsList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Open a new connection
            conn = new DBConnect().connectionClass();
            if (conn != null) {
                String query = "SELECT * FROM notifications WHERE user_id = ? ORDER BY notifications_id DESC";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, userId);  // Bind the user_id parameter
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Notifications notification = new Notifications();
                    notification.setNotifications_id(rs.getInt("notifications_id"));
                    notification.setNotifications_content(rs.getString("notifications_content"));
                    notification.setCreated_at(rs.getString("created_at"));
                    notification.setUser_id(rs.getString("user_id"));
                    notification.setViewed(rs.getBoolean("viewed"));
                    notificationsList.add(notification);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Ensure all resources are closed properly
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return notificationsList;
    }


    public static ArrayList<Notifications> initNotificationList2() {
        return new ArrayList<>();
    }
    public static void markAllNotificationsAsViewed() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = new DBConnect().connectionClass();
            if (conn != null) {
                String query = "UPDATE notifications SET viewed = 1  WHERE viewed = 0";
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void saveNotification(Notifications notification) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = new DBConnect().connectionClass();
            if (conn != null) {
                String query = "INSERT INTO notifications (notifications_content, created_at, user_id, viewed) " +
                        "VALUES ('" + notification.getNotifications_content() + "', '" + notification.getCreated_at() + "', '" + notification.getUser_id() + "', " + (notification.isViewed() ? 1 : 0) + ")";
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

