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
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn = dbConnect.connectionClass();

    public static ArrayList<Notifications> getNotifications() {
        ArrayList<Notifications> notificationsList = new ArrayList<>();
        if (conn != null) {
            String query = "SELECT * FROM notifications";
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
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
                    notification.setUser_id(rs.getString("user_id"));
                    notificationsList.add(notification);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return notificationsList;
    }
}
