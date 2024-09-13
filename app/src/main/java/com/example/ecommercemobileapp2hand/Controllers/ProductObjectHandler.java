package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.ProductObject;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductObjectHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection connection;
    public static ArrayList<ProductObject> getData()
    {
        connection = dbConnect.connectionClass();
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
        return list;
    }
}
