package com.example.ecommercemobileapp2hand.Controllers;


import com.example.ecommercemobileapp2hand.Models.Size;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SizeHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static ArrayList<Size> getData(){
        ArrayList<Size> list = new ArrayList<>();
        conn = dbConnect.connectionClass();
        if(conn!=null){
            String query = "Select * from size";
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    Size size = new Size();
                    size.setSize_id(rs.getInt(1));
                    size.setSize_name(rs.getString(2));
                    list.add(size);

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
