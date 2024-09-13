package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BagHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

//    public static ArrayList<Bag> getData()
//    {
//        ArrayList<Bag> lst = new ArrayList<>();
//        conn = dbConnect.connectionClass();
//        if(conn!=null) {
//            String query = "Select * from bag";
//            try
//            {
//                Statement stmt = conn.createStatement();
//                ResultSet rs = stmt.executeQuery(query);
//                while (rs.next())
//                {
//
//                    Bag bag = new Bag(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getInt(4));
//                    lst.add(bag);
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            } finally {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return lst;
//    }
    public static boolean addBag(Bag bag) {
        conn = dbConnect.connectionClass();
        boolean isSuccess = false;
        if (conn != null) {
            String query = "INSERT INTO Bag (user_id, product_details_size_id, amount) VALUES (?, ?, ?)";
            try {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, bag.getUser_id());
                pstmt.setInt(2, bag.getProduct_details_size_id());
                pstmt.setInt(3, bag.getAmount());
                int rowsAffected = pstmt.executeUpdate();
                isSuccess = rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

}
