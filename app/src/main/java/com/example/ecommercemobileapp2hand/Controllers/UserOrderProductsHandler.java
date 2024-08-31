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

                    userOrderProducts.setUser_order_id(rs.getInt(1));
                    userOrderProducts.setProduct_details_size_id(rs.getInt(2));
                    userOrderProducts.setAmount(rs.getInt(3));
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

    public static int getAmountItems(int id)
    {
        try
        {
            conn = dbConnect.connectionClass();
            String sql = "SELECT COUNT(*) FROM USER_ORDER_PRODUCTS WHERE USER_ORDER_PRODUCTS.user_order_id = " + id + "";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next())
            {
                int amount = rs.getInt(1);
                st.close();
                rs.close();
                conn.close();
                return amount;
            }
            else
            {
                st.close();
                rs.close();
                conn.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
}
