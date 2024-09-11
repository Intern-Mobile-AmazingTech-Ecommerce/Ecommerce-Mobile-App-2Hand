package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
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

public class UserOrderHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static void updateOrderStatus(int userOrderId, int newOrderStatusId) {
        conn = dbConnect.connectionClass();
        String selectQuery = "SELECT COUNT(*) FROM order_status WHERE order_status_id = ?";
        String updateQuery = "UPDATE user_order SET order_status_id = ? WHERE user_order_id = ?";

        try {
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Kiểm tra xem trạng thái mới có tồn tại trong bảng `order_status`
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, newOrderStatusId);
                ResultSet resultSet = selectStmt.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // Nếu mã trạng thái mới tồn tại, thực hiện cập nhật
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, newOrderStatusId);
                        updateStmt.setInt(2, userOrderId);
                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            conn.commit(); // Commit nếu thành công
                            System.out.println("Order status updated successfully.");
                        } else {
                            conn.rollback(); // Rollback nếu thất bại
                            System.out.println("Failed to update order status.");
                        }
                    }
                } else {
                    System.out.println("Invalid order_status_id.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true); // Đặt lại chế độ auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
