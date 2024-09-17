package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Coupon;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import android.util.Log;

public class CouponHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static final String TAG = "CouponHandler";

    public static void getCoupons(Callback<ArrayList<Coupon>> callback) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Connection conn = dbConnect.connectionClass();
            ArrayList<Coupon> coupons = new ArrayList<>();
            if (conn != null) {
                try {
                    Statement stmt = conn.createStatement();
                    String query = "SELECT * FROM Coupons"; // Adjust query as needed
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        Coupon coupon = new Coupon();
                        coupon.setCouponId(rs.getInt("coupon_id")); // Adjusted column name
                        coupon.setCode(rs.getString("code"));
                        coupon.setDiscountType(rs.getString("discount_type"));
                        coupon.setDiscountValue(rs.getBigDecimal("discount_value"));
                        coupon.setMinOrderValue(rs.getBigDecimal("min_order_value"));
                        coupon.setStartDate(rs.getDate("start_date"));
                        coupon.setEndDate(rs.getDate("end_date"));
                        coupon.setIsActive(rs.getBoolean("is_active"));
                        coupons.add(coupon);
                    }
                    if (coupons.isEmpty()) {
                        Log.d(TAG, "No coupons found in the database.");
                    } else {
                        Log.d(TAG, "Coupons fetched: " + coupons.size());
                    }
                    callback.onResult(coupons);
                } catch (SQLException e) {
                    Log.e(TAG, "SQL Exception: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        Log.e(TAG, "SQL Exception on close: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e(TAG, "Connection is null");
            }
            shutDownExecutor(service);
        });
    }

    public static void getCouponsByProductIds(List<Integer> productIds, Callback<ArrayList<Coupon>> callback) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Connection conn = dbConnect.connectionClass();
            ArrayList<Coupon> coupons = new ArrayList<>();
            if (conn != null) {
                try {
                    Statement stmt = conn.createStatement();
                    String query = "SELECT * FROM Coupons WHERE product_id IN (" + productIds.toString().replace("[", "").replace("]", "") + ")";
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        Coupon coupon = new Coupon();
                        coupon.setCouponId(rs.getInt("coupon_id"));
                        coupon.setCode(rs.getString("code"));
                        coupon.setDiscountType(rs.getString("discount_type"));
                        coupon.setDiscountValue(rs.getBigDecimal("discount_value"));
                        coupon.setMinOrderValue(rs.getBigDecimal("min_order_value"));
                        coupon.setStartDate(rs.getDate("start_date"));
                        coupon.setEndDate(rs.getDate("end_date"));
                        coupon.setIsActive(rs.getBoolean("is_active"));
                        coupons.add(coupon);
                    }
                    if (coupons.isEmpty()) {
                        Log.d(TAG, "No coupons found for the given product IDs.");
                    } else {
                        Log.d(TAG, "Coupons fetched: " + coupons.size());
                    }
                    callback.onResult(coupons);
                } catch (SQLException e) {
                    Log.e(TAG, "SQL Exception: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        Log.e(TAG, "SQL Exception on close: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e(TAG, "Connection is null");
            }
            shutDownExecutor(service);
        });
    }

    public static void getCouponsByOrderAmount(BigDecimal orderAmount, Callback<ArrayList<Coupon>> callback) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Connection conn = dbConnect.connectionClass();
            ArrayList<Coupon> coupons = new ArrayList<>();
            if (conn != null) {
                try {
                    Statement stmt = conn.createStatement();
                    String query = "SELECT * FROM Coupons WHERE min_order_value <= " + orderAmount;
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        Coupon coupon = new Coupon();
                        coupon.setCouponId(rs.getInt("coupon_id"));
                        coupon.setCode(rs.getString("code"));
                        coupon.setDiscountType(rs.getString("discount_type"));
                        coupon.setDiscountValue(rs.getBigDecimal("discount_value"));
                        coupon.setMinOrderValue(rs.getBigDecimal("min_order_value"));
                        coupon.setStartDate(rs.getDate("start_date"));
                        coupon.setEndDate(rs.getDate("end_date"));
                        coupon.setIsActive(rs.getBoolean("is_active"));
                        coupons.add(coupon);
                    }
                    if (coupons.isEmpty()) {
                        Log.d(TAG, "No coupons found for the given order amount.");
                    } else {
                        Log.d(TAG, "Coupons fetched: " + coupons.size());
                    }
                    callback.onResult(coupons);
                } catch (SQLException e) {
                    Log.e(TAG, "SQL Exception: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        Log.e(TAG, "SQL Exception on close: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e(TAG, "Connection is null");
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