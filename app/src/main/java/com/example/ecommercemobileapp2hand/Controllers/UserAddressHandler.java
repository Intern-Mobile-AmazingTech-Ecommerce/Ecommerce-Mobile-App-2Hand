package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserAddressHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn;

    public static void getListAdressByUserId(String userId, Callback<ArrayList<UserAddress>> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            ArrayList<UserAddress> list = new ArrayList<>();
            Connection conn = dbConnect.connectionClass();
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            if (conn != null) {
                try {
                    String sql = "SELECT user_address_id, user_address_street, user_address_city, user_address_state, user_address_zipcode, user_address_phone FROM user_address WHERE user_id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, userId);
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                        UserAddress address = new UserAddress();
                        address.setUser_address_id(rs.getInt("user_address_id"));
                        address.setUser_address_street(rs.getString("user_address_street"));
                        address.setUser_address_city(rs.getString("user_address_city"));
                        address.setUser_address_state(rs.getString("user_address_state"));
                        address.setUser_address_zipcode(rs.getString("user_address_zipcode"));
                        address.setUser_address_phone(rs.getString("user_address_phone"));
                        list.add(address);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            callback.onResult(list);
        });

    }
    public static UserAddress GetUserAddressByAddressID(int userAddressId){
        UserAddress address = new UserAddress();
        conn = dbConnect.connectionClass();
        if (conn != null) {
            try {
                CallableStatement cstmt = conn.prepareCall("call GetUserAddressByAddressID(?)");
                cstmt.setInt(1, userAddressId);
                ResultSet rs = cstmt.executeQuery();
                if (rs.next()) {
                    address.setUser_address_id(rs.getInt("user_address_id"));
                    address.setUser_address_street(rs.getString("user_address_street"));
                    address.setUser_address_city(rs.getString("user_address_city"));
                    address.setUser_address_state(rs.getString("user_address_state"));
                    address.setUser_address_zipcode(rs.getString("user_address_zipcode"));
                    address.setUser_address_phone(rs.getString("user_address_phone"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return address;
    }
    public static void insertAddress(String UserID,String street,String city,String state, String zipcode,String phoneNumber, Callback<Boolean> callback){
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            conn = dbConnect.connectionClass();
            try{
                String sql = "Insert into user_address values (?,?,?,?,?,?)";
                if(conn!=null){
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1,UserID);
                    preparedStatement.setString(2,street);
                    preparedStatement.setString(3,city);
                    preparedStatement.setString(4,state);
                    preparedStatement.setString(5,zipcode);
                    preparedStatement.setString(6,phoneNumber);
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
    public static void updateAddressById(int addressId, String street, String city, String state, String zip, String phone,Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            Connection conn = dbConnect.connectionClass();
            PreparedStatement pstmt = null;

            if (conn != null) {
                try {
                    String sql = "UPDATE user_address SET user_address_street = ?, user_address_city = ?, user_address_state = ?, user_address_zipcode = ?, user_address_phone = ? WHERE user_address_id = ?";
                    pstmt = conn.prepareStatement(sql);
                    System.out.println(street);
                    System.out.println(city);
                    System.out.println(state);
                    pstmt.setString(1, street);
                    pstmt.setString(2, city);
                    pstmt.setString(3, state);
                    pstmt.setString(4, zip);
                    pstmt.setString(5, phone);
                    pstmt.setInt(6,addressId);

                    int rowsUpdated = pstmt.executeUpdate();
                    callback.onResult(rowsUpdated > 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (pstmt != null) pstmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            shutDownExecutor(service);
        });

    }
    public static void deleteAddressById(int addressId, Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            Connection conn = dbConnect.connectionClass();
            PreparedStatement pstmt = null;

            if (conn != null) {
                try {
                    String sql = "DELETE FROM user_address WHERE user_address_id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, addressId);

                    int rowsDeleted = pstmt.executeUpdate();
                    callback.onResult( rowsDeleted > 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (pstmt != null) pstmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
