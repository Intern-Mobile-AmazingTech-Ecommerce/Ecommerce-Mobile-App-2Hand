package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderStatusHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static ArrayList<OrderStatus> getData()
    {
        ArrayList<OrderStatus> lst = new ArrayList<>();
        conn = dbConnect.connectionClass();
        if(conn!=null) {
            String query = "Select * from order_status";
            try
            {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next())
                {
                    if (!rs.getString(2).equals("Order Placed") && !rs.getString(2).equals("Order Confirmed"))
                    {
                        OrderStatus ord = new OrderStatus(rs.getInt(1), rs.getString(2));
                        lst.add(ord);
                    }
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
       return lst;
    }
}
