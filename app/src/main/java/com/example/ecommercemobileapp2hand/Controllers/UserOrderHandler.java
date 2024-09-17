package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserOrderHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static void updateOrderStatus(int userOrderId, int newOrderStatusId, Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
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
//                                System.out.println("Order status updated successfully.");
                                callback.onResult(true);
                            } else {
                                conn.rollback(); // Rollback nếu thất bại
//                                System.out.println("Failed to update order status.");
                                callback.onResult(false);
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
            shutDownExecutor(service);
        });

    }
    public static UserOrder GetLatestUserOrder (String userID){
        conn = dbConnect.connectionClass();
        UserOrder userOrder=new UserOrder();
        if(conn!=null) {
            try
            {
                CallableStatement cstmt = conn.prepareCall("call GetLatestOrder(?)");
                cstmt.setString(1, userID);
                ResultSet rs = cstmt.executeQuery();
                if (rs.next()){
                    userOrder.setUser_order_id(rs.getInt(1));
                    userOrder.setUser_id(rs.getString(2));
                    userOrder.setUser_address_id(rs.getInt(3));
                    userOrder.setTotal_price(rs.getBigDecimal(4));
                    userOrder.setOrder_status_id(rs.getInt(5));
                    userOrder.setCreated_at(rs.getString(6));
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
        return userOrder;
    }
    public static int createUserOrder(String userID,int userAddressID){
        conn = dbConnect.connectionClass();
        int userOrderID=-1;
        if(conn!=null) {
            try
            {
                CallableStatement cstmt = conn.prepareCall("call CreateUserOrder(?,?)");
                cstmt.setString(1, userID);
                cstmt.setInt(2,userAddressID);
                ResultSet rs = cstmt.executeQuery();
                if (rs.next()){
                    userOrderID=rs.getInt(1);
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
        return userOrderID;
    }

    private static void shutDownExecutor(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    public interface Callback<T> {
        void onResult(T result);
    }

}
