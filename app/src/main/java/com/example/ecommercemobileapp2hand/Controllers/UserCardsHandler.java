package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserCards;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserCardsHandler {
    private static DBConnect dbConnect = new DBConnect();
    public static void getListCardByUserId(String userId, Callback<ArrayList<UserCards>> callback)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            ArrayList<UserCards> list = new ArrayList<>();
            Connection conn = dbConnect.connectionClass();
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            if (conn != null) {
                try {
                    String sql = "SELECT user_cards_id, user_card_number, user_card_ccv, user_card_exp, user_card_holder_name FROM user_cards WHERE user_id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, userId);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        UserCards card = new UserCards();
                        card.setUser_cards_id(rs.getInt("user_cards_id"));
                        card.setUser_card_number(rs.getString("user_card_number"));
                        card.setUser_card_ccv(rs.getString("user_card_ccv"));
                        card.setUser_card_exp(rs.getString("user_card_exp"));
                        card.setUser_card_holder_name(rs.getString("user_card_holder_name"));
                        list.add(card);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            callback.onResult(list);
            shutDownExecutor(service);
        });

    }
    public static void updateByCardId(int cardId, String cardNumber, String cardCcv, String cardExp, String cardHolderName,Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            Connection conn = dbConnect.connectionClass();
            PreparedStatement pstmt = null;

            if (conn != null) {
                try {
                    String sql = "UPDATE user_cards SET user_card_number = ?, user_card_ccv = ?, user_card_exp = ?, user_card_holder_name = ? WHERE user_cards_id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, cardNumber);
                    pstmt.setString(2, cardCcv);
                    pstmt.setString(3, cardExp);
                    pstmt.setString(4, cardHolderName);
                    pstmt.setInt(5, cardId);

                    int rowsUpdated = pstmt.executeUpdate();
                    callback.onResult(rowsUpdated > 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (pstmt != null) pstmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            shutDownExecutor(service);
        });

    }
    public static void insertCard(String userID, String cardNumber, String ccv, String exp, String holderName, Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            Connection conn = dbConnect.connectionClass();

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                String expFormatted = outputFormat.format(inputFormat.parse(exp)); // Chuyển đổi ngày

                String sql = "Insert into user_cards values(?,?,?,?,?)";
                if (conn != null) {
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, userID);
                    preparedStatement.setString(2, cardNumber);
                    preparedStatement.setString(3, ccv);
                    preparedStatement.setString(4, expFormatted);  // Sử dụng exp đã được định dạng lại
                    preparedStatement.setString(5, holderName);
                    int rs = preparedStatement.executeUpdate();
                    callback.onResult(rs > 0);
                }
            } catch (SQLException | ParseException e) {
                throw new RuntimeException(e.getMessage());
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            shutDownExecutor(service);
        });
    }
    public static boolean deleteCardById(int cardId) {
        Connection conn = dbConnect.connectionClass();
        PreparedStatement pstmt = null;

        if (conn != null) {
            try {
                String sql = "DELETE FROM user_cards WHERE user_cards_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, cardId);

                int rowsDeleted = pstmt.executeUpdate();
                return rowsDeleted > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
