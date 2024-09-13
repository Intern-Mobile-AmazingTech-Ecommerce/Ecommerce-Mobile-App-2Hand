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

public class WishlistHandler {
    private static DBConnect dbConnect = new DBConnect();
    private static Connection conn ;
    static ObjectMapper objectMapper = new ObjectMapper();
    public static ArrayList<Product> getWishListDetailByWishListID(int wishListID){
        conn = dbConnect.connectionClass();
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
                    Timestamp timestamp = rs.getTimestamp(6);
                    if (timestamp != null) {
                        LocalDateTime localDateTime = timestamp.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        p.setCreated_at(localDateTime);
                    }
                    //Object
                    ProductObject obj = new ProductObject();
                    obj.setProduct_object_id(7);
                    obj.setObject_name(rs.getString(8));
                    p.setProductObject(obj);
                    //Category
                    ProductCategory category = new ProductCategory();
                    category.setProduct_category_id(rs.getInt(9));
                    category.setProduct_category_name(rs.getString(10));
                    category.setProduct_category_description(rs.getString(11));
                    category.setProduct_category_thumbnail(rs.getString(12));
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

        return list;
    }
    public static ArrayList<Wishlist> getWishListByUserID(String userID){
        conn = dbConnect.connectionClass();
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
    public static Boolean addNewWishlist(String UserID,String wishListName){
        conn = dbConnect.connectionClass();
        try{
            if(conn != null){
                String sql = "Insert Into wishlist VALUES (?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,UserID);
                preparedStatement.setString(2,wishListName);
                int result =preparedStatement.executeUpdate();
                return result > 0;
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
    public static boolean checkProductDetailsExistsInWishlist(int product_details_id, int wishlist_id){
        conn = dbConnect.connectionClass();
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

    public static boolean checkProductDetailsExistsInWishlistByUserID(int product_details_id, String userID){
        conn = dbConnect.connectionClass();
        PreparedStatement stmt = null;
        try{
            if(conn!=null){
                String sql = "select COUNT(*)\n" +
                        "from wishlist_product wp \n" +
                        "inner join wishlist w on w.wishlist_id = wp.wishlist_id\n" +
                        "where product_details_id = ? and w.user_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, product_details_id);
                stmt.setString(2, userID);
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
    public static Boolean insertToWishlist(int wishlist_id,int product_details_id){
        conn = dbConnect.connectionClass();
        PreparedStatement preparedStatement = null;
        try{
            if(conn!=null){
                String sql = "Insert into wishlist_product values(?,?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1,wishlist_id);
                preparedStatement.setInt(2,product_details_id);
                int rs = preparedStatement.executeUpdate();
                return rs>0;
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
    public static Boolean removeFromWishlist(int wishlist_id,int product_details_id){
        conn = dbConnect.connectionClass();
        PreparedStatement preparedStatement = null;
        try{
            if(conn!=null){
                String sql = "Delete from wishlist_product where wishlist_product.wishlist_id = ? and wishlist_product.product_details_id = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1,wishlist_id);
                preparedStatement.setInt(2,product_details_id);
                int rs = preparedStatement.executeUpdate();
                return rs>0;
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

    public static void clearWishlist(int wishList_ID) {
        conn = dbConnect.connectionClass();
        PreparedStatement preparedStatement = null;
        try {
            if (conn != null) {
                String sql = "DELETE FROM wishlist_product WHERE wishlist_id = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, wishList_ID);

                int result = preparedStatement.executeUpdate();

                if (result > 0) {
                    System.out.println("Wishlist cleared successfully.");
                } else {
                    System.out.println("No products found in the wishlist.");
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
    }


}
