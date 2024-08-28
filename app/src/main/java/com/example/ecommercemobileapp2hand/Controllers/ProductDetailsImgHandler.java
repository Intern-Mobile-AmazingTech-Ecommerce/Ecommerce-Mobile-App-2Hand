package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.ProductDetailsImg;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDetailsImgHandler
{
    private static DBConnect dbConnect = new DBConnect();
    private static Connection connection;
    public static ArrayList<ProductDetailsImg> getData()
    {
        connection = dbConnect.connectionClass();
        ArrayList<ProductDetailsImg> list = new ArrayList<>();
        if (connection !=null)
        {
            String query = "Select * from product_details_img";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next())
                {
                    ProductDetailsImg productDetailsImg = new ProductDetailsImg();
                    productDetailsImg.getProduct_img_id(resultSet.getInt(1));
                    productDetailsImg.getProduct_details_id(resultSet.getInt(2));
                    list.add(productDetailsImg);
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
        return list;
    }
}
