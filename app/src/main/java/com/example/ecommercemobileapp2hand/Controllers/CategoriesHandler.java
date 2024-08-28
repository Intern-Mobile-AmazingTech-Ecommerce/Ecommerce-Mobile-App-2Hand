package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CategoriesHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn ;

    public static ArrayList<ProductCategory> getData(){
        ArrayList<ProductCategory> list = new ArrayList<>();
        conn = dbConnect.connectionClass();
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
        return list;
    }
}
