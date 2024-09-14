package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public static ArrayList<OrderStatus> getData1()
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
                    if (!rs.getString(2).equals("Processing"))
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

    public static boolean CheckStatus(int id, String str)
    {
        try
        {
            conn = dbConnect.connectionClass();
            String sql = "SELECT order_status_name FROM ORDER_STATUS WHERE order_status_id = " + id + "";
            Statement st = conn.createStatement();
            ResultSet rs =  st.executeQuery(sql);
            if (rs.next() && rs.getString(1).equals(str))
            {
                st.close();
                rs.close();
                conn.close();
                return true;
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
        return false;
    }
    private static void shutDownExecutor(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
    public interface Callback<T> {
        void onResult(T result);
    }
}
