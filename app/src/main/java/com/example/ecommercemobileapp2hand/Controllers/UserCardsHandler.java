package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserCards;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserCardsHandler {
    private static DBConnect dbConnect = new DBConnect();
    public static ArrayList<UserCards> getListCardByUserId(String userId)
    {
        ArrayList<UserCards> list = new ArrayList<>();
        Connection conn = dbConnect.connectionClass();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                String sql = "SELECT user_cards_id, user_card_number, user_card_ccv, user_card_exp, user_card_holder_name FROM user_cards WHERE user_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userId);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    UserCards card = new UserCards();
                    card.setUser_cards_id(rs.getInt("user_cards_id"));
                    card.setUser_card_number(rs.getString("user_card_number"));
                    card.setUser_card_ccv(rs.getString("user_card_ccv"));
                    card.setUser_card_exp(rs.getString("user_card_exp"));
                    card.setUser_card_holder_name(rs.getString("user_card_holder_name"));
                    list.add(card);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    public static boolean updateByCardId(int cardId, String cardNumber, String cardCcv, String cardExp, String cardHolderName) {
        Connection conn = dbConnect.connectionClass();
        PreparedStatement pstmt = null;

        if (conn != null) {
            try {
                String sql = "UPDATE user_cards SET user_card_number = ?, user_card_ccv = ?, user_card_exp = ?, user_card_holder_name = ? WHERE user_cards_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, cardNumber);
                pstmt.setString(2, cardCcv);
                pstmt.setString(3, cardExp);
                pstmt.setString(4, cardHolderName);
                pstmt.setInt(5, cardId);

                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    public static Boolean insertCard(String userID,String cardNumber,String ccv,String exp,String holderName){
        Connection conn = dbConnect.connectionClass();
        conn = dbConnect.connectionClass();

        try{
            String sql = "Insert into user_cards values(?,?,?,?,?)";
            if(conn!=null){
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,userID);
                preparedStatement.setString(2,cardNumber);
                preparedStatement.setString(3,ccv);
                preparedStatement.setString(4,exp);
                preparedStatement.setString(5,holderName);
                int rs = preparedStatement.executeUpdate();
                return rs>0;
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }finally {
            try{
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return false;
    }
    public static boolean deleteCardById(int cardId) {
        Connection conn = dbConnect.connectionClass();
        PreparedStatement pstmt = null;

        if (conn != null) {
            try {
                String sql = "DELETE FROM user_cards WHERE user_cards_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, cardId);

                int rowsDeleted = pstmt.executeUpdate();
                return rowsDeleted > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
