package com.example.ecommercemobileapp2hand.Controllers;

import android.widget.Toast;

import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class BagHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static ArrayList<Bag> getData(String userID)
    {
        ArrayList<Bag> lst = new ArrayList<>();
        conn = dbConnect.connectionClass();
        if(conn!=null) {
            try
            {
                CallableStatement cstmt = conn.prepareCall("call CheckUserBag(?)");
                cstmt.setString(1, userID);
                ResultSet rs = cstmt.executeQuery();
                while (rs.next())
                {
                    Bag bag =new Bag();
                    bag.setBag_id(rs.getInt(1));
                    bag.setUser_id(rs.getString(2));
                    bag.setProduct_details_size_id(rs.getInt(3));
                    bag.setProduct_details_id(rs.getInt(4));
                    bag.setAmount(rs.getInt(5));
                    bag.setSize(rs.getString(6));
                    bag.setProduct_id(rs.getInt(7));
                    bag.setProduct_name(rs.getString(8));
                    bag.setColor(rs.getString(9));
                    bag.setImage(rs.getString(10));
                    bag.setBasePrice(rs.getBigDecimal(11));
                    bag.setSalePrice(rs.getBigDecimal(12));
                    lst.add(bag);
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
    public static boolean updateProductAmount(String userID,int productDetailSizeID, int amount){
        conn = dbConnect.connectionClass();
        if(conn!=null) {
            try
            {
                CallableStatement cstmt = conn.prepareCall("call UpdateUserBagAmount(?,?,?)");
                cstmt.setString(1, userID);
                cstmt.setInt(2, productDetailSizeID);
                cstmt.setInt(3, amount);
                cstmt.executeUpdate();

            } catch (SQLException e) {
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public static boolean deleteUserBag(String userID){
        conn = dbConnect.connectionClass();
        if(conn!=null) {
            try
            {
                CallableStatement cstmt = conn.prepareCall("call DeleteUserBag(?)");
                cstmt.setString(1,userID);
                cstmt.executeUpdate();
            } catch (SQLException e) {
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public static void addBag(Bag bag,Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
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
                    callback.onResult(isSuccess);
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
        shutDownExecutor(service);
        });

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
    // Method to add a product to the bag using the stored procedure
    public static void addToBag(Bag bag, Callback<Boolean> callback) {
        try {
            // Get a connection to the database
            conn = dbConnect.connectionClass();

            // Create the SQL query to call the stored procedure
            String query = "{CALL CheckProductBagDuplicated(?, ?, ?)}";

            // Create a CallableStatement to execute the stored procedure
            CallableStatement stmt = conn.prepareCall(query);
            stmt.setString(1, bag.getUser_id());
            stmt.setInt(2, bag.getProduct_details_size_id());
            stmt.setInt(3, bag.getAmount());

            // Execute the stored procedure
            int rowsAffected = stmt.executeUpdate();
            boolean result = rowsAffected > 0; // Check if any rows were affected

            // Use the callback to return the result
            if (callback != null) {
                callback.onResult(result);
            }

            // Close the statement and connection
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onResult(false);  // Return false if an error occurs
            }
        }
    }


}
