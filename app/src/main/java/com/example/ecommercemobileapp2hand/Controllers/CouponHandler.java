package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Coupon;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

    public static Coupon getDiscountValueFromDatabase(String couponCode) {
        String query = "SELECT coupon_id, code, discount_type, discount_value, min_order_value, start_date, end_date, is_active FROM Coupons WHERE code = ?";
        Coupon coupon = null;

        try (Connection conn = dbConnect.connectionClass();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, couponCode);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int couponId = rs.getInt("coupon_id");
                    String code = rs.getString("code");
                    String discountType = rs.getString("discount_type");
                    BigDecimal discountValue = rs.getBigDecimal("discount_value");
                    BigDecimal minOrderValue = rs.getBigDecimal("min_order_value");
                    Date startDate = rs.getDate("start_date");
                    Date endDate = rs.getDate("end_date");
                    boolean isActive = rs.getBoolean("is_active");

                    java.sql.Date sqlStartDate = startDate != null ? new java.sql.Date(startDate.getTime()) : null;
                    java.sql.Date sqlEndDate = endDate != null ? new java.sql.Date(endDate.getTime()) : null;

                    coupon = new Coupon(couponId, code, discountType, discountValue, minOrderValue, sqlStartDate, sqlEndDate, isActive);
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQL Exception: " + e.getMessage(), e);
        }

        return coupon;
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