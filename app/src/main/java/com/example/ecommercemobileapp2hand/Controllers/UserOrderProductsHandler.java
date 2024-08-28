package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserOrderProducts;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
public class UserOrderProductsHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn = dbConnect.connectionClass();

    public static ArrayList<UserOrderProducts> getUserOrderProducts() {
        ArrayList<UserOrderProducts> userOrderProductsList = new ArrayList<>();
        if (conn != null) {
            String query = "SELECT * FROM UserOrderProducts";
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    UserOrderProducts userOrderProducts = new UserOrderProducts();
                    userOrderProducts.setUser_order_id(rs.getString("user_order_id"));
                    userOrderProducts.setProduct_details_id(rs.getInt("product_details_id"));
                    userOrderProducts.setAmount(rs.getInt("amount"));
                    userOrderProductsList.add(userOrderProducts);
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
        return userOrderProductsList;
    }
}
