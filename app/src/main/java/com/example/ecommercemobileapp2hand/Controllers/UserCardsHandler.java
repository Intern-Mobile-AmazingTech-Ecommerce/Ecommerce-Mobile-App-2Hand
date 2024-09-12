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
    private static Connection conn;
    public static Boolean insertCard(String userID,String cardNumber,String ccv,String exp,String holderName){
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
}
