package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserOrderProducts;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserOrderProductsHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static void getUserOrderProductsByOrderID(int userorderid,Callback<ArrayList<UserOrderProducts>> callback)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
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
                        UserOrderProducts userOrderProducts = new UserOrderProducts(rs.getInt(1) ,rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getBigDecimal(8), rs.getBigDecimal(9), rs.getBigDecimal(10), rs.getBoolean(11));

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
            callback.onResult(userOrderProductsList);
            shutDownExecutor(service);
        });

    }

    public static void getAmountItems(int id, Callback<Integer> callback)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
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
                    callback.onResult(amount);
                }
                else
                {
                    st.close();
                    rs.close();
                    conn.close();
                    callback.onResult(0);
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        shutDownExecutor(service);
        });

    }
    public static void getProductDetailsID(int productid, String colorname, String sizename,Callback<Integer> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            conn = dbConnect.connectionClass();
            if (conn != null) {
                try {
                    // Gọi stored procedure với 3 tham số
                    CallableStatement cstmt = conn.prepareCall("{ call getProductDetailsID (?, ?, ?) }");
                    cstmt.setInt(1, productid);
                    cstmt.setString(2, colorname);
                    cstmt.setString(3, sizename);

                    ResultSet rs = cstmt.executeQuery();
                    if (rs.next()) {
                        callback.onResult(rs.getInt(1));
                        shutDownExecutor(service);
                    }else {
                        callback.onResult(0);
                        shutDownExecutor(service);
                    }
                    rs.close();
                    cstmt.close();
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

        });
    }
    public static void updateIsReviewed(int userOrderId, int productDetailsSizeId, boolean isReviewed, Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            Connection conn = null;
            boolean result = false;
            try {
                conn = dbConnect.connectionClass();
                if (conn != null) {
                    try {
                        PreparedStatement pstmt = conn.prepareStatement("UPDATE user_order_products SET isReviewed = ? WHERE user_order_id = ? AND product_details_size_id = ?");
                        pstmt.setBoolean(1, isReviewed);
                        pstmt.setInt(2, userOrderId);
                        pstmt.setInt(3, productDetailsSizeId);

                        int rowsAffected = pstmt.executeUpdate();
                        result = rowsAffected > 0;
                        pstmt.close();
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                callback.onResult(result);
                shutDownExecutor(service);
            }
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
