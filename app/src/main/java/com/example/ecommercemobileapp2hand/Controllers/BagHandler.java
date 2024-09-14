package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
}
