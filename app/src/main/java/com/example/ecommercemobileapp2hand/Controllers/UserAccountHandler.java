package com.example.ecommercemobileapp2hand.Controllers;

import android.content.Context;
import android.widget.Toast;

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

    public static UserAccount getUserAccountById(int userId) {
        UserAccount userAccount = null;
        conn = dbConnect.connectionClass();
        String sql = "{call GetDetailsUserAccount(?)}";
        try {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Khởi tạo UserAccount với dữ liệu từ resultSet
                        userAccount = new UserAccount(
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

                        // Lấy dữ liệu từ JSON để phân tích các thuộc tính danh sách
                        String wishlistJson = resultSet.getString("wishlist_array");
                        String notificationsJson = resultSet.getString("notifications_array");
                        String cardsJson = resultSet.getString("cards_array");
                        String ordersJson = resultSet.getString("order_array");
                        String addressesJson = resultSet.getString("address_array");

                        // Phân tích JSON thành các danh sách tương ứng
                        userAccount.setLstWL(parseJson(wishlistJson, Wishlist.class));
                        userAccount.setLstNoti(parseJson(notificationsJson, Notifications.class));
                        userAccount.setLstCard(parseJson(cardsJson, UserCards.class));
                        userAccount.setLstOrder(parseJson(ordersJson, UserOrder.class));
                        userAccount.setLstAddress(parseJson(addressesJson, UserAddress.class));
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
        return userAccount;
    }

    private static <T> ArrayList<T> parseJson(String json, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
        if (json != null && !json.isEmpty()) {
            try {
                Gson gson = new Gson();
                Type listType = TypeToken.getParameterized(ArrayList.class, clazz).getType();
                list = gson.fromJson(json, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static int getUserAccount (String email, String pass)
    {
        try
        {
            conn = dbConnect.connectionClass();
            String sql = "SELECT user_id FROM USER_ACCOUNT WHERE EMAIL = ? AND PASSWORD = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,email);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt(1);
                rs.close();
                pst.close();
                conn.close();
                return id;
            }
            rs.close();
            pst.close();
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
}