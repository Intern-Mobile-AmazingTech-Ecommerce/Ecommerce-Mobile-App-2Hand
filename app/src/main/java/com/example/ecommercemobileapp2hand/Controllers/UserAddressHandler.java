package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserAddressHandler {
    private static DBConnect dbConnect = new DBConnect();

    public static List<UserAddress> getUserAddressesByUserId(int userId) {
        List<UserAddress> userAddresses = new ArrayList<>();
        String sql = "SELECT * FROM user_address WHERE user_id = ?";
        Connection connection = null;
        try {
            connection = dbConnect.connectionClass();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        UserAddress userAddress = new UserAddress(
                                resultSet.getInt("user_address_id"),
                                resultSet.getInt("user_id"),
                                resultSet.getString("user_address_street"),
                                resultSet.getString("user_address_city"),
                                resultSet.getString("user_address_state"),
                                resultSet.getString("user_address_zipcode")
                        );
                        userAddresses.add(userAddress);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return userAddresses;
    }
}
