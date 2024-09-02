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
    private static Connection conn;

    public static ArrayList<UserOrderProducts> getUserOrderProductsByOrderID(int userorderid) {
        ArrayList<UserOrderProducts> userOrderProductsList = new ArrayList<>();
        conn = dbConnect.connectionClass();
        if (conn != null) {
            String query = "SELECT product.product_id, product.product_name, product.thumbnail, product_color.product_color_name, size.size_name, user_order_products.amount, product.base_price, product_details.sale_price, user_order.total_price\n" +
                    "FROM user_order_products\n" +
                    "JOIN user_order ON user_order.user_order_id = user_order_products.user_order_id\n" +
                    "JOIN product_details_size ON product_details_size.product_details_size_id = user_order_products.product_details_size_id\n" +
                    "JOIN size ON size.size_id = product_details_size.size_id\n" +
                    "JOIN product_details ON product_details_size.product_details_id = product_details.product_details_id\n" +
                    "JOIN product_color ON product_color.product_color_id = product_details.product_color_id\n" +
                    "JOIN product ON product.product_id = product_details.product_id\n" +
                    "WHERE user_order_products.user_order_id = " + userorderid;
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    UserOrderProducts userOrderProducts = new UserOrderProducts(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getBigDecimal(7), rs.getBigDecimal(8), rs.getBigDecimal(9));

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
