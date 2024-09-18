package com.example.ecommercemobileapp2hand.Controllers;

import android.database.sqlite.SQLiteDatabase;

import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Models.ProductObject;
import com.example.ecommercemobileapp2hand.Models.Wishlist;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WishlistHandler {
    private static DBConnect dbConnect = new DBConnect();

    static ObjectMapper objectMapper = new ObjectMapper();
    public static void getWishListDetailByWishListID(int wishListID, Callback<ArrayList<Product>> callback){
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
           Connection conn = dbConnect.connectionClass();
            objectMapper.registerModule(new JavaTimeModule());
            ArrayList<Product> list = new ArrayList<>();
            if(conn!=null){
                try{
                    CallableStatement cstmt = conn.prepareCall("call GetWishlistDetail(?)");
                    cstmt.setInt(1, wishListID);
                    ResultSet rs = cstmt.executeQuery();
                    while (rs.next()){
                        Product p = new Product();
                        p.setProduct_id(rs.getInt(1));
                        p.setProduct_name(rs.getString(2));
                        p.setThumbnail(rs.getString(3));
                        p.setBase_price(rs.getBigDecimal(4));
                        p.setSold(rs.getBigDecimal(5));
                        p.setIsFreeship(rs.getBoolean(6));
                        Timestamp timestamp = rs.getTimestamp(7);
                        if (timestamp != null) {
                            LocalDateTime localDateTime = timestamp.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime();
                            p.setCreated_at(localDateTime);
                        }
                        p.setCoupon_id(8);
                        //Object
                        ProductObject obj = new ProductObject();
                        obj.setProduct_object_id(9);
                        obj.setObject_name(rs.getString(10));
                        p.setProductObject(obj);
                        //Category
                        ProductCategory category = new ProductCategory();
                        category.setProduct_category_id(rs.getInt(11));
                        category.setProduct_category_name(rs.getString(12));
                        category.setProduct_category_description(rs.getString(13));
                        category.setProduct_category_thumbnail(rs.getString(14));
                        p.setProductCategory(category);

                        //Array Pro Details
                        String productDetailsJson = rs.getString("product_details_array");
                        ArrayList<ProductDetails> productDetails = objectMapper.readValue(
                                productDetailsJson,
                                new TypeReference< ArrayList<ProductDetails>>() {}
                        );

                        p.setProductDetailsArrayList(productDetails);
                        list.add(p);

                    }

                }catch (SQLException e){
                    throw new RuntimeException(e);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }finally {
                    try{
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
    public static ArrayList<Wishlist> getWishListByUserID(String userID){
        Connection conn = dbConnect.connectionClass();
        ArrayList<Wishlist> list = new ArrayList<>();
        objectMapper.registerModule(new JavaTimeModule());
        if(conn!=null){
            try{
                CallableStatement cstmt = conn.prepareCall("call getWishListByUserID(?)");
                cstmt.setString(1, userID);
                ResultSet rs = cstmt.executeQuery();
                while (rs.next()){
                    Wishlist wishlist = new Wishlist();
                    wishlist.setWishlist_id(rs.getInt(1));
                    wishlist.setWishlist_name(rs.getString(2));
                    wishlist.setWishlist_quantity(rs.getInt(3));
                    list.add(wishlist);
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
    public static void addNewWishlist(String UserID,String wishListName, Callback<Boolean> callback){
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            Connection conn = dbConnect.connectionClass();
            try{
                if(conn != null){
                    String sql = "Insert Into wishlist VALUES (?,?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1,UserID);
                    preparedStatement.setString(2,wishListName);
                    int result =preparedStatement.executeUpdate();
                    callback.onResult(result >0);
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
        shutDownExecutor(service);
        });

    }
    public static boolean checkProductDetailsExistsInWishlist(int product_details_id, int wishlist_id){
        Connection conn = dbConnect.connectionClass();
        PreparedStatement stmt = null;
        try{
            if(conn!=null){
                String sql = "SELECT COUNT(*) " +
                        "FROM wishlist_product wp " +
                        "INNER JOIN wishlist w ON w.wishlist_id = wp.wishlist_id " +
                        "WHERE wp.product_details_id = ? AND wp.wishlist_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, product_details_id);
                stmt.setInt(2, wishlist_id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    return rs.getInt(1) > 0;
                }
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
        return false;
    }

    public static void checkProductDetailsExistsInWishlistByUserID(int product_details_id, String userID, Callback<Boolean> callback) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Connection conn = null;
            ResultSet resultSet = null;
            PreparedStatement stmt = null;

            try {
                Thread.sleep(1000);
                conn = dbConnect.connectionClass();
                if (conn == null || conn.isClosed()) {
                    throw new SQLException("Connection is closed or null");
                }
                String sql = "SELECT COUNT(*) " +
                        "FROM wishlist_product wp " +
                        "INNER JOIN wishlist w ON w.wishlist_id = wp.wishlist_id " +
                        "WHERE product_details_id = ? AND w.user_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, product_details_id);
                stmt.setString(2, userID);
                resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    callback.onResult(resultSet.getInt(1) > 0);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (stmt != null) stmt.close();
                    if (conn != null && !conn.isClosed()) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            shutDownExecutor(service);
        });
    }

    public static void insertToWishlist(int wishlist_id,int product_details_id,Callback<Boolean> callback){
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
           Connection conn = dbConnect.connectionClass();
            PreparedStatement preparedStatement = null;
            try{
                if(conn!=null){
                    String sql = "Insert into wishlist_product values(?,?)";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1,wishlist_id);
                    preparedStatement.setInt(2,product_details_id);
                    int rs = preparedStatement.executeUpdate();
                    callback.onResult(rs>0);
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
            shutDownExecutor(service);
        });

    }
    public static void removeFromWishlist(int wishlist_id,int product_details_id, Callback<Boolean> callback){
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
           Connection conn = dbConnect.connectionClass();
            PreparedStatement preparedStatement = null;
            try{
                if(conn!=null){
                    String sql = "Delete from wishlist_product where wishlist_product.wishlist_id = ? and wishlist_product.product_details_id = ?";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1,wishlist_id);
                    preparedStatement.setInt(2,product_details_id);
                    int rs = preparedStatement.executeUpdate();
                    callback.onResult(rs>0);
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
            shutDownExecutor(service);
        });

    }

    public static void clearWishlist(int wishList_ID, Callback<Boolean> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
           Connection conn = dbConnect.connectionClass();
            PreparedStatement preparedStatement = null;
            try {
                if (conn != null) {
                    String sql = "DELETE FROM wishlist_product WHERE wishlist_id = ?";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, wishList_ID);

                    int result = preparedStatement.executeUpdate();
                    callback.onResult(result > 0);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            shutDownExecutor(service);
        });

    }

    public static void removeWishlist(int wishlist_ID,Callback<Boolean> callback )
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
          Connection  conn = dbConnect.connectionClass();
            PreparedStatement preparedStatement = null;
            try {
                if (conn != null) {
                    String sql = "DELETE FROM wishlist WHERE wishlist_id = ?";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, wishlist_ID);

                    int result = preparedStatement.executeUpdate();

                    if (result > 0) {
//                        System.out.println("Wishlist has removed.");
                        callback.onResult(true);
                    } else {
//                        System.out.println("No found wishlist.");
                        callback.onResult(false);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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
    public static boolean isConnectionValid() {
        Connection connection = dbConnect.connectionClass();
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public interface Callback<T> {
        void onResult(T result);
    }
}
