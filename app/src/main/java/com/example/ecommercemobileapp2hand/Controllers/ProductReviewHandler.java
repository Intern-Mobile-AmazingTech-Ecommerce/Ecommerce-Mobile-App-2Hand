package com.example.ecommercemobileapp2hand.Controllers;


import com.example.ecommercemobileapp2hand.Models.ProductReview;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProductReviewHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;


    public static void getData(Callback<ArrayList<ProductReview>> callback){
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            ArrayList<ProductReview> list = new ArrayList<>();
            conn = dbConnect.connectionClass();
            if(conn!=null){
                String query = "Select * from product_review";
                try{
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()){
                        ProductReview product_rv = new ProductReview();
                        product_rv.setProduct_review_id(rs.getInt(1));
                        product_rv.setUser_id(rs.getString(2));
                        product_rv.setProduct_details_id(rs.getInt(3));
                        product_rv.setReview_content(rs.getString(4));
                        product_rv.setRating(rs.getInt(5));
                        Timestamp timestamp = rs.getTimestamp(6);
                        if(timestamp!=null){
                            LocalDateTime createdAt = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            product_rv.setCreated_at(createdAt);
                        }
                        list.add(product_rv);

                    }

                }catch (SQLException e){
                    throw new RuntimeException(e);
                }finally {
                    try{
                        conn.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }

            callback.onResult(list);
            shutDownExecutor(service);
        });



    }
    public static void insertReview(ProductReview review, Callback<Boolean> callback)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            Connection conn = dbConnect.connectionClass();

            try{
                String sql = "Insert into product_review values (?,?,?,?,?)";
                if(conn!=null){
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, review.getUser_id());
                    preparedStatement.setInt(2, review.getProduct_details_id());
                    preparedStatement.setString(3, review.getReview_content());
                    preparedStatement.setInt(4, review.getRating());

                    Timestamp timestamp = Timestamp.valueOf(review.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    preparedStatement.setTimestamp(5, timestamp);

                    int rs = preparedStatement.executeUpdate();
                    callback.onResult(rs > 0);
                }


            }catch (SQLException e){
                throw new RuntimeException(e.getMessage());

            }finally {
                try{
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
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
