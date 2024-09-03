package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserOrderProducts;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.CallableStatement;
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
        if (conn != null)
        {
            try
            {
                CallableStatement cstmt = conn.prepareCall("{ call getUserOrderProductsByOrderID (?) }");
                cstmt.setInt(1, userorderid);
                ResultSet rs = cstmt.executeQuery();
                while (rs.next())
                {
                    UserOrderProducts userOrderProducts = new UserOrderProducts(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getBigDecimal(7), rs.getBigDecimal(8), rs.getBigDecimal(9));

                    userOrderProductsList.add(userOrderProducts);
                }
                rs.close();
                cstmt.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                try
                {
                    conn.close();
                } catch (SQLException e)
                {
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
