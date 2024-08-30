package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDetailsHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection connection;

//    public static ArrayList<ProductDetails> getData() {
//        connection = dbConnect.connectionClass();
//        ArrayList<ProductDetails> list = new ArrayList<>();
//        if (connection != null) {
//            String query = "Select * from product_details";
//            try {
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery(query);
//                while (resultSet.next()) {
//                    ProductDetails productDetails = new ProductDetails();
//                    productDetails.setProduct_details_id(resultSet.getInt(1));
//                    productDetails.setProduct_id(resultSet.getInt(2));
//                    productDetails.setProduct_color_id(resultSet.getInt(3));
//                    productDetails.setDescription(resultSet.getString(4));
//                    productDetails.setSale_price(resultSet.getBigDecimal(5));
//                    list.add(productDetails);
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            } finally {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return list;
//    }
}
