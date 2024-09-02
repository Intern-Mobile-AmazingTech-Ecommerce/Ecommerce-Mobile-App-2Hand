package com.example.ecommercemobileapp2hand.Controllers;

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

import java.sql.CallableStatement;
import java.sql.Connection;
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
    public static ArrayList<Wishlist> getWishListByUserID(int userID){
        conn = dbConnect.connectionClass();
        ArrayList<Wishlist> list = new ArrayList<>();
        if(conn!=null){
            try{
                CallableStatement cstmt = conn.prepareCall("call GetWishlist(?)");
                cstmt.setInt(1, userID);
                ResultSet rs = cstmt.executeQuery();
                while (rs.next()){
                    Wishlist wishlist = new Wishlist();
                    wishlist.setWishlist_id(rs.getInt(1));
                    wishlist.setWishlist_name(rs.getString(2));
                    wishlist.setWishlist_quantity((rs.getInt(3)));
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
}
