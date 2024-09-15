package com.example.ecommercemobileapp2hand.Controllers;

import android.util.Log;

import com.example.ecommercemobileapp2hand.Models.Notifications;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.UserCards;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.Models.Wishlist;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserAccountHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    //lấy tt user
//    public static UserAccount getUserAccountByEmail(String email) {
//
//        UserAccount userAccount = null;
//        try (Connection conn = dbConnect.connectionClass();
//             PreparedStatement pstmt = conn.prepareStatement("SELECT user_id, email, first_name, last_name, phone_number, img_url, gender, age_range FROM user_account WHERE email = ?")) {
//
//            pstmt.setString(1, email);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    String userId = rs.getString("user_id");
//                    String firstName = rs.getString("first_name");
//                    String lastName = rs.getString("last_name");
//                    String phoneNumber = rs.getString("phone_number");
//                    String imgUrl = rs.getString("img_url");
//                    String gender = rs.getString("gender");
//                    String ageRange = rs.getString("age_range");
//
//                    userAccount = new UserAccount(userId, email, firstName, lastName, phoneNumber, imgUrl, gender, ageRange);
//
//                }
//            }
//        } catch (SQLException e) {
//            Log.e("DB_QUERY", "Lỗi truy vấn: " + e.getMessage());
//        }
//        return userAccount;
//
//    }

    //lưu tt user vào db khi signin bằng google or facebook
    public static void saveUserAccount(String email, String displayName, String provider) {
        conn = dbConnect.connectionClass();
        if (conn == null) {
            return;
        }

        PreparedStatement pstmt = null;
        try {
            String userId = generateRandomId();

            String[] nameParts = displayName.split(" ", 2);
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            String query = "IF NOT EXISTS (SELECT 1 FROM user_account WHERE email = ?) " +
                    "BEGIN " +
                    "    INSERT INTO user_account (user_id, email, first_name, last_name) " +
                    "    VALUES (?, ?, ?, ?) " +
                    "END";

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, userId);
            pstmt.setString(3, email);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);

            pstmt.executeUpdate();

            Log.w("UserAccountHandler", "Luu thanh cong.");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("UserAccountHandler", "luu du lieu that bai: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String generateRandomId() {
        return UUID.randomUUID().toString();
    }


    //lưu tt user
    public static void saveUserToDB(String firstName, String lastName, String email, String gender, String ageRange) {
        String checkQuery = "SELECT COUNT(*) FROM user_account WHERE email = ?";
        String insertQuery = "INSERT INTO user_account (user_id, email, first_name, last_name, gender, age_range) VALUES (?, ?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE user_account SET gender = ?, age_range = ? WHERE email = ?";

        try (Connection conn = dbConnect.connectionClass();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            if (conn == null) {
                Log.e("DB_CONNECT", "Database connection failed.");
                return;
            }

            checkStmt.setString(1, email);
            try (ResultSet rs = checkStmt.executeQuery()) {
                boolean userExists = rs.next() && rs.getInt(1) > 0;

                if (userExists) {
                    updateStmt.setString(1, gender);
                    updateStmt.setString(2, ageRange);
                    updateStmt.setString(3, email);
                    updateStmt.executeUpdate();
                } else {
                    String userId = generateRandomId();
                    insertStmt.setString(1, userId);
                    insertStmt.setString(2, email);
                    insertStmt.setString(3, firstName);
                    insertStmt.setString(4, lastName);
                    insertStmt.setString(5, gender);
                    insertStmt.setString(6, ageRange);
                    insertStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            Log.e("DB_OPERATION", "Database operation failed: " + e.getMessage());
        }
    }


    public static void updateUserAccountDetails(String firstName, String lastName, String phoneNumber, String imgUrl, String email, Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();

        service.execute(() -> {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = dbConnect.connectionClass();
                if (conn != null) {
                    String updateQuery;
                    if (imgUrl == null || imgUrl.isEmpty()) {
                        updateQuery = "UPDATE user_account SET first_name = ?, last_name = ?, phone_number = ? WHERE email = ?";
                        pstmt = conn.prepareStatement(updateQuery);
                        pstmt.setString(1, firstName);
                        pstmt.setString(2, lastName);
                        pstmt.setString(3, phoneNumber);
                        pstmt.setString(4, email);
                    } else {
                        updateQuery = "UPDATE user_account SET first_name = ?, last_name = ?, phone_number = ?, img_url = ? WHERE email = ?";
                        pstmt = conn.prepareStatement(updateQuery);
                        pstmt.setString(1, firstName);
                        pstmt.setString(2, lastName);
                        pstmt.setString(3, phoneNumber);
                        pstmt.setString(4, imgUrl);
                        pstmt.setString(5, email);
                    }

                    int rowsUpdated = pstmt.executeUpdate();
                    callback.onResult(rowsUpdated > 0);
                } else {
                    Log.e("DB_UPDATE", "Kết nối thất bại.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            shutDownExecutor(service);
        });
    }

    //kt email đã tồn tại chưa
    public static void checkEmailExists(String email, Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            boolean emailExists = false;
            Connection conn = dbConnect.connectionClass();
            if (conn != null) {
                try {
                    String query = "SELECT COUNT(*) FROM user_account WHERE email = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, email);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        emailExists = resultSet.getInt(1) > 0;
                    }
                    resultSet.close();
                    preparedStatement.close();
                } catch (Exception ex) {
                    Log.e("Error", ex.getMessage());
                } finally {
                    try {
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            callback.onResult(emailExists);
            shutDownExecutor(service);
        });

    }

    public static void getUserAccount (String email, Callback<UserAccount> callback)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            UserAccount userAccount = null;
            conn = dbConnect.connectionClass();
            String sql = "{call GetDetailsUserAccount(?)}";
            try (CallableStatement callableStatement = conn.prepareCall(sql)) {
                callableStatement.setString(1, email);
                try (ResultSet resultSet = callableStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userAccount = new UserAccount(
                                resultSet.getString("user_id"),
                                resultSet.getString("email"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("img_url"),
                                resultSet.getString("gender"),
                                resultSet.getString("age_range")
                        );
                        String wishlistJson = resultSet.getString("wishlist_array");
                        String notificationsJson = resultSet.getString("notifications_array");
                        String cardsJson = resultSet.getString("cards_array");
                        String ordersJson = resultSet.getString("order_array");
                        String addressesJson = resultSet.getString("address_array");

                        userAccount.setLstWL(parseJson(wishlistJson, Wishlist.class));
                        userAccount.setLstNoti(parseJson(notificationsJson, Notifications.class));
                        userAccount.setLstCard(parseJson(cardsJson, UserCards.class));
                        userAccount.setLstOrder(parseJson(ordersJson, UserOrder.class));
                        userAccount.setLstAddress(parseJson(addressesJson, UserAddress.class));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            callback.onResult(userAccount);
            shutDownExecutor(service);
        });

    }
    public static void getUserAccountByuserID (String userID, Callback<UserAccount> callback)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(()->{
            UserAccount userAccount = null;
            conn = dbConnect.connectionClass();
            String sql = "{call GetDetailsUserAccountByUserID(?)}";
            try (CallableStatement callableStatement = conn.prepareCall(sql)) {
                callableStatement.setString(1, userID);
                try (ResultSet resultSet = callableStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userAccount = new UserAccount(
                                resultSet.getString("user_id"),
                                resultSet.getString("email"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("img_url"),
                                resultSet.getString("gender"),
                                resultSet.getString("age_range")
                        );

                        String wishlistJson = resultSet.getString("wishlist_array");
                        String notificationsJson = resultSet.getString("notifications_array");
                        String cardsJson = resultSet.getString("cards_array");
                        String ordersJson = resultSet.getString("order_array");
                        String addressesJson = resultSet.getString("address_array");

                        userAccount.setLstWL(parseJson(wishlistJson, Wishlist.class));
                        userAccount.setLstNoti(parseJson(notificationsJson, Notifications.class));
                        userAccount.setLstCard(parseJson(cardsJson, UserCards.class));
                        userAccount.setLstOrder(parseJson(ordersJson, UserOrder.class));
                        userAccount.setLstAddress(parseJson(addressesJson, UserAddress.class));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            callback.onResult(userAccount);
            shutDownExecutor(service);
        });

    }
    private static <T> ArrayList<T> parseJson(String json, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
        if (json != null && !json.isEmpty()) {
            try {
                Gson gson = new Gson();
                Type listType = TypeToken.getParameterized(ArrayList.class, clazz).getType();
                list = gson.fromJson(json, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
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
