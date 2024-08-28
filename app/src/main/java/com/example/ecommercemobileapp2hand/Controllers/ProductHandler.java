package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class ProductHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn ;

    public static ArrayList<Product> getData(){
        conn = dbConnect.connectionClass();
        ArrayList<Product> list = new ArrayList<>();
        if(conn!=null){
            String query = "Select * from product";
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    Product product = new Product();
                    product.setProduct_id(rs.getInt(1));
                    product.setProduct_name(rs.getString(2));
                    product.setProduct_object_id(rs.getInt(3));
                    product.setProduct_category_id(rs.getInt(4));
                    product.setThumbnail(rs.getString(5));
                    product.setBase_price(rs.getBigDecimal(6));
                    Timestamp timestamp = rs.getTimestamp(7);
                    if(timestamp!=null){
                        LocalDateTime createdAt = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        product.setCreated_at(createdAt);
                    }
                    list.add(product);

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

        return list;
    }
}
