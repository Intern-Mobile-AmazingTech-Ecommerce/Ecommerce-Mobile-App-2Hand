package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserCards;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCardsHandler {
    private static DBConnect dbConnect = new DBConnect();

    public boolean addUserCard(UserCards userCard) {
        String sql = "INSERT INTO user_cards (user_id, user_card_number, user_card_ccv, user_card_exp, user_card_holder_name) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = dbConnect.connectionClass();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userCard.getUserId());
                statement.setString(2, userCard.getCardNumber());
                statement.setString(3, userCard.getCcv());
                statement.setDate(4, userCard.getExpirationDate());
                statement.setString(5, userCard.getCardHolderName());
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean updateUserCard(UserCards userCard) {
        String sql = "UPDATE user_cards SET user_card_number = ?, user_card_ccv = ?, user_card_exp = ?, user_card_holder_name = ? WHERE user_cards_id = ?";
        Connection connection = null;
        try {
            connection = dbConnect.connectionClass();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userCard.getCardNumber());
                statement.setString(2, userCard.getCcv());
                statement.setDate(3, userCard.getExpirationDate());
                statement.setString(4, userCard.getCardHolderName());
                statement.setInt(5, userCard.getUserCardsId());
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean deleteUserCard(int userCardsId) {
        String sql = "DELETE FROM user_cards WHERE user_cards_id = ?";
        Connection connection = null;
        try {
            connection = dbConnect.connectionClass();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userCardsId);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public UserCards getUserCardById(int userCardsId) {
        String sql = "SELECT * FROM user_cards WHERE user_cards_id = ?";
        Connection connection = null;
        try {
            // Obtain the connection from DBConnect
            connection = dbConnect.connectionClass();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userCardsId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new UserCards(
                                resultSet.getInt("user_cards_id"),
                                resultSet.getInt("user_id"),
                                resultSet.getString("user_card_number"),
                                resultSet.getString("user_card_ccv"),
                                resultSet.getDate("user_card_exp"),
                                resultSet.getString("user_card_holder_name")
                        );
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
        return null;
    }
}
