package com.example.ecommercemobileapp2hand.Controllers;

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

public class CategoriesHandler {
    private static DBConnect dbConnect = new DBConnect();
   // private static Connection conn ;

    public static void getData(Callback<ArrayList<ProductCategory>> callback){
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            ArrayList<ProductCategory> list = new ArrayList<>();
            Connection conn = dbConnect.connectionClass();
            if(conn!=null){
                String query = "Select * from product_category";
                try{

                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next()){
                        ProductCategory category = new ProductCategory();
                        category.setProduct_category_id(rs.getInt(1));
                        category.setProduct_category_name(rs.getString(2));
                        category.setProduct_category_description(rs.getString(3));
                        category.setProduct_category_thumbnail(rs.getString(4));
                        list.add(category);
                    }

                }catch (SQLException e){
                    throw new RuntimeException(e);
                }finally {
                    try {
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
