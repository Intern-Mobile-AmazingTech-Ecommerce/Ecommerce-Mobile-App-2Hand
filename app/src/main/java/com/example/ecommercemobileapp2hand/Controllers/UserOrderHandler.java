package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class UserOrderHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static ArrayList<UserOrder> getOrder(int id)
    {
        ArrayList<UserOrder> lst = new ArrayList<>();
        conn = dbConnect.connectionClass();
        if(conn!=null) {
            String query = "SELECT * FROM USER_ORDER WHERE USER_ID = " + id + "";
            try
            {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {

                    LocalDateTime createdAt = null;

                    if(rs.getTimestamp(6) != null)
                    {
                        createdAt = rs.getTimestamp(6).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }
                    UserOrder ord = new UserOrder(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getInt(5), createdAt);
                    lst.add(ord);
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
