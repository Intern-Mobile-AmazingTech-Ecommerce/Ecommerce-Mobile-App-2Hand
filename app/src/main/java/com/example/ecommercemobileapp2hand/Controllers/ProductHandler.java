package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.Models.ProductObject;
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

//    public static ArrayList<Product> getData(){
//        conn = dbConnect.connectionClass();
//        ArrayList<Product> list = new ArrayList<>();
//        if(conn!=null){
//            String query = "Select * from product";
//            try{
//                Statement stmt = conn.createStatement();
//                ResultSet rs = stmt.executeQuery(query);
//                while (rs.next()){
//                    Product product = new Product();
//                    product.setProduct_id(rs.getInt(1));
//                    product.setProduct_name(rs.getString(2));
//                    product.setProduct_object_id(rs.getInt(3));
//                    product.setProduct_category_id(rs.getInt(4));
//                    product.setThumbnail(rs.getString(5));
//                    product.setBase_price(rs.getBigDecimal(6));
//                    Timestamp timestamp = rs.getTimestamp(7);
//                    if(timestamp!=null){
//                        LocalDateTime createdAt = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//                        product.setCreated_at(createdAt);
//                    }
//                    list.add(product);
//
//                }
//
//            }catch (SQLException e){
//                throw new RuntimeException(e);
//            }finally {
//                try{
//                    conn.close();
//                }catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return list;
//    }

    public static ArrayList<Product> getDataByObjectName(String objName){
        conn = dbConnect.connectionClass();
        ArrayList<Product> list = new ArrayList<>();
        if(conn!=null){
            String query = "select product.product_id,product.product_name,product.thumbnail,product.base_price,product.created_at, product_object.*, product_category.* from product\n" +
                    "inner join product_object on product_object.product_object_id = product.product_object_id\n" +
                    "inner join product_category on product_category.product_category_id = product.product_category_id\n" +
                    "where product_object.object_name = '" + objName+"'";
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()){
                    Product p = new Product();
                    p.setProduct_id(rs.getInt(1));
                    p.setProduct_name(rs.getString(2));
                    p.setThumbnail(rs.getString(3));
                    p.setBase_price(rs.getBigDecimal(4));
                    Timestamp timestamp = rs.getTimestamp(5);
                    if (timestamp != null) {
                        LocalDateTime localDateTime = timestamp.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        p.setCreated_at(localDateTime);
                    }
                    //Object
                    ProductObject obj = new ProductObject();
                    obj.setProduct_object_id(6);
                    obj.setObject_name(rs.getString(7));
                    p.setProductObject(obj);
                    //Category
                    ProductCategory category = new ProductCategory();
                    category.setProduct_category_id(rs.getInt(8));
                    category.setProduct_category_name(rs.getString(9));
                    category.setProduct_category_description(rs.getString(10));
                    category.setProduct_category_thumbnail(rs.getString(11));
                    p.setProductCategory(category);

                    list.add(p);

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
