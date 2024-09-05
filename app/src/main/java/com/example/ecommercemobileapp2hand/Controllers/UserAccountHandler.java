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

public class UserAccountHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static void saveUserAccount(String email, String displayName, String provider) {
        conn = dbConnect.connectionClass();
        if (conn == null) {
            return;
        }

        PreparedStatement pstmt = null;
        try {
            String[] nameParts = displayName.split(" ", 2);
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            String dummyPassword = "dummy_password";

            String query = "MERGE INTO user_account AS target " +
                    "USING (SELECT ? AS email) AS source " +
                    "ON (target.email = source.email) " +
                    "WHEN MATCHED THEN " +
                    "    UPDATE SET username = ?, first_name = ?, last_name = ?, gender = 'Unspecified', phone_number = NULL " +
                    "WHEN NOT MATCHED THEN " +
                    "    INSERT (username, password, gender, email, phone_number, first_name, last_name) " +
                    "    VALUES (?, ?, 'Unspecified', ?, NULL, ?, ?)";

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, displayName);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, dummyPassword);
            pstmt.setString(6, email);
            pstmt.setString(7, firstName);
            pstmt.setString(8, lastName);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveUserToDB(String firstName, String lastName, String email, String password) {
        DBConnect dbConnect = new DBConnect();
        Connection conn = null;
        PreparedStatement pstmt = null;

        String username = firstName + lastName + (int) (Math.random() * 1000);

        try {
            conn = dbConnect.connectionClass();

            if (conn != null) {
                String insertQuery = "INSERT INTO user_account (username, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, username);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, email);
                pstmt.setString(5, password);

                pstmt.executeUpdate();
                Log.d("DB_INSERT", "Dữ liệu đã được lưu vào cơ sở dữ liệu");
            } else {
                Log.e("DB_INSERT", "Kết nối cơ sở dữ liệu thất bại");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB_INSERT", "Lỗi khi lưu vào cơ sở dữ liệu: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void updateUserDetails(String email, String gender) {
        DBConnect dbConnect = new DBConnect();
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnect.connectionClass();

            if (conn != null) {
                String updateQuery = "UPDATE user_account SET gender = ? WHERE email = ?";
                pstmt = conn.prepareStatement(updateQuery);
                pstmt.setString(1, gender);
                pstmt.setString(2, email);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    Log.d("DB_UPDATE", "Dữ liệu đã được update");
                } else {
                    Log.e("DB_UPDATE", "Không tìm thấy người dùng với email: " + email);
                }
            } else {
                Log.e("DB_UPDATE", "Kết nối thất bại");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //    private void updateUserDetails(String gender, String ageRange) {
//        DBConnect dbConnect = new DBConnect();
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//
//        String email = getIntent().getStringExtra("email");
//
//        try {
//            conn = dbConnect.connectionClass();
//
//            if (conn != null) {
//                String updateQuery = "UPDATE user_account SET gender = ?, age_range = ? WHERE email = ?";
//                pstmt = conn.prepareStatement(updateQuery);
//                pstmt.setString(1, gender);
//                pstmt.setString(2, ageRange);
//                pstmt.setString(3, email);
//
//                int rowsUpdated = pstmt.executeUpdate();
//                if (rowsUpdated > 0) {
//                    Log.d("DB_UPDATE", "Dữ liệu đã được cập nhật");
//                } else {
//                    Log.e("DB_UPDATE", "Không tìm thấy người dùng với email: " + email);
//                }
//            } else {
//                Log.e("DB_UPDATE", "Kết nối thất bại");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (pstmt != null) pstmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
