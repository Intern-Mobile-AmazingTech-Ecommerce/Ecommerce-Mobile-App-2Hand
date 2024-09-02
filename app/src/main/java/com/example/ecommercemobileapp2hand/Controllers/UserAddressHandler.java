package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserAddressHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static String getAddress(int id)
    {
        try
        {
            conn = dbConnect.connectionClass();
            String sql = "SELECT USER_ADDRESS_STREET, USER_ADDRESS_CITY, USER_ADDRESS_STATE, USER_ADDRESS_ZIPCODE, user_account.phone_number FROM USER_ADDRESS JOIN USER_ORDER ON USER_ORDER.user_address_id = user_address.user_address_id JOIN USER_ACCOUNT ON USER_ORDER.user_id = user_account.user_id WHERE user_order.user_order_id = " + id + "";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next())
            {
                String s = rs.getString(1) + " " + rs.getString(2) + ", " + rs.getString(3) + " " + rs.getString(4) + "\n" + rs.getString(5);
                rs.close();
                st.close();
                conn.close();
                return s;
            }
            else
            {
                rs.close();
                st.close();
                conn.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
