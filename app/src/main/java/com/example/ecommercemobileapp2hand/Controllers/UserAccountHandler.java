package com.example.ecommercemobileapp2hand.Controllers;

import android.content.Context;
import android.widget.Toast;

import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static UserAccount getUserAccountById(String userId) {
        String sql = "SELECT * FROM user_account WHERE user_id = ?";
        conn = dbConnect.connectionClass();
        try {
            try (PreparedStatement statement = conn.prepareStatement(sql))
            {
                statement.setString(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new UserAccount(
                                resultSet.getInt("user_id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("gender"),
                                resultSet.getString("email"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("img_url")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static UserAccount getUserAccount (String email, String pass)
    {
        UserAccount userAccount = null;
        try
        {
            conn = dbConnect.connectionClass();
            String sql = "SELECT * FROM USER_ACCOUNT WHERE EMAIL = ? AND PASSWORD = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,email);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
            if (rs.next())
            {
                userAccount = new UserAccount(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));
            }
            rs.close();
            pst.close();
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return userAccount;
    }
}