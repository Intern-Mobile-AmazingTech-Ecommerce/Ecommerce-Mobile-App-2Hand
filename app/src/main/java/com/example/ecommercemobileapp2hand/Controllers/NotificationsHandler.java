package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Notifications;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class NotificationsHandler {
    public static ArrayList<Notifications> getNotifications() {
        ArrayList<Notifications> notificationsList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Open a new connection
            conn = new DBConnect().connectionClass();
            if (conn != null) {
                String query = "SELECT * FROM notifications";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Notifications notification = new Notifications();
                    notification.setNotifications_id(rs.getInt("notifications_id"));
                    notification.setNotifications_content(rs.getString("notifications_content"));
                    // Convert Timestamp to LocalDateTime
                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        LocalDateTime localDateTime = timestamp.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        notification.setCreated_at(localDateTime);
                    }
                    notification.setUser_id(rs.getInt("user_id"));
                    notificationsList.add(notification);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Ensure all resources are closed properly
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return notificationsList;
    }
}

