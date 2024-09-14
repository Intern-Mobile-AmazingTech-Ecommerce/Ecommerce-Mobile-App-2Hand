package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.ProductObject;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProductObjectHandler {
    private static DBConnect dbConnect = new DBConnect();

    public static void getData(Callback<ArrayList<ProductObject>> callback)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            Connection connection = dbConnect.connectionClass();
            ArrayList<ProductObject> list = new ArrayList<ProductObject>();
            if (connection != null)
            {
                String query = "Select * from product_object";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next())
                    {
                        ProductObject productObject = new ProductObject();
                        productObject.setProduct_object_id(resultSet.getInt(1));
                        productObject.setObject_name(resultSet.getString(2));
                        list.add(productObject);
                    }
                }catch (SQLException e)
                {
                    throw new RuntimeException(e);
                }finally {
                    try{
                        connection.close();
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
