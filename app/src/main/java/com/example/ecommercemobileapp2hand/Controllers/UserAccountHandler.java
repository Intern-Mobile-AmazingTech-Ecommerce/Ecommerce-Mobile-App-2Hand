package com.example.ecommercemobileapp2hand.Controllers;

import android.util.Log;

import com.example.ecommercemobileapp2hand.Models.Notifications;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.UserCards;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.Models.Wishlist;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class UserAccountHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    //lấy tt user
    public static UserAccount getUserAccountByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserAccount userAccount = null;

        try {
            conn = dbConnect.connectionClass();
            if (conn != null) {
                String query = "SELECT user_id, email, first_name, last_name, phone_number, img_url, gender, age_range " +
                        "FROM user_account WHERE email = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, email);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    String userId = rs.getString("user_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phoneNumber = rs.getString("phone_number");
                    String imgUrl = rs.getString("img_url");
                    String gender = rs.getString("gender");
                    String ageRange = rs.getString("age_range");

                    userAccount = new UserAccount(userId, email, firstName, lastName, phoneNumber, imgUrl, gender, ageRange);
                }
            } else {
                Log.e("DB_QUERY", "Kết nối thất bại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB_QUERY", "Lỗi truy vấn: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userAccount;
    }

    //lưu tt user vào db khi signin bằng google or facebook
    public static void saveUserAccount(String email, String displayName, String provider) {
        conn = dbConnect.connectionClass();
        if (conn == null) {
            return;
        }

        PreparedStatement pstmt = null;
        try {
            String userId = generateRandomId();

            String[] nameParts = displayName.split(" ", 2);
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            String query = "IF NOT EXISTS (SELECT 1 FROM user_account WHERE email = ?) " +
                    "BEGIN " +
                    "    INSERT INTO user_account (user_id, email, first_name, last_name) " +
                    "    VALUES (?, ?, ?, ?) " +
                    "END";

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, userId);
            pstmt.setString(3, email);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);

            pstmt.executeUpdate();

            Log.w("UserAccountHandler", "Luu thanh cong.");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("UserAccountHandler", "luu du lieu that bai: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    //lưu tt user
    public static void saveUserToDB(String firstName, String lastName, String email, String gender, String ageRange) {
        conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnect.connectionClass();
            if (conn == null) {
                System.out.println("Không thể kết nối db.");
                return;
            }
            String checkQuery = "SELECT COUNT(*) FROM user_account WHERE email = ?";
            pstmt = conn.prepareStatement(checkQuery);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            boolean userExists = false;
            if (rs.next()) {
                userExists = rs.getInt(1) > 0;
            }
            rs.close();
            pstmt.close();

            if (userExists) {
                String updateQuery = "UPDATE user_account SET gender = ?, age_range = ? WHERE email = ?";
                pstmt = conn.prepareStatement(updateQuery);
                pstmt.setString(1, gender);
                pstmt.setString(2, ageRange);
                pstmt.setString(3, email);
            } else {
                String userId = generateRandomId();
                String insertQuery = "INSERT INTO user_account (user_id, email, first_name, last_name, gender, age_range) VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, userId);
                pstmt.setString(2, email);
                pstmt.setString(3, firstName);
                pstmt.setString(4, lastName);
                pstmt.setString(5, gender);
                pstmt.setString(6, ageRange);
            }

            Log.d("UserAccountHandler", "Saving user to database...");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("UserAccountHandler", "Error saving user to database: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //cập nhật tt chi tiết của user
    public static void updateUserAccountDetails(String firstName, String lastName, String phoneNumber, String imgUrl, String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnect.connectionClass();
            if (conn != null) {
                String updateQuery = "UPDATE user_account SET first_name = ?, last_name = ?, phone_number = ?, img_url = ? WHERE email = ?";
                pstmt = conn.prepareStatement(updateQuery);

                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, phoneNumber);
                pstmt.setString(4, imgUrl);
                pstmt.setString(5, email);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    Log.d("DB_UPDATE", "Cập nhật thành công.");
                } else {
                    Log.e("DB_UPDATE", "Không tìm thấy user.");
                }
            } else {
                Log.e("DB_UPDATE", "Kết nối thất bại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure the database resources are closed
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //kt email đã tồn tại chưa
    public boolean checkEmailExists(String email) {
        boolean emailExists = false;
        conn = dbConnect.connectionClass();
        if (conn != null) {
            try {
                String query = "SELECT COUNT(*) FROM user_account WHERE email = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    emailExists = resultSet.getInt(1) > 0;
                }
                resultSet.close();
                preparedStatement.close();
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
            }
        }
        return emailExists;
    }

}
