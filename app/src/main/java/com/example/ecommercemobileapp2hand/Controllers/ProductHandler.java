package com.example.ecommercemobileapp2hand.Controllers;

import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Models.ProductObject;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.CallableStatement;
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
    private static Connection conn;
    static ObjectMapper objectMapper = new ObjectMapper();

    public static ArrayList<Product> getData() {
        objectMapper.registerModule(new JavaTimeModule());
        conn = dbConnect.connectionClass();
        ArrayList<Product> list = new ArrayList<>();
        if (conn != null) {
            try {
                CallableStatement cstmt = conn.prepareCall("call GetProductDetails");
                ResultSet rs = cstmt.executeQuery();
                while (rs.next()) {
                    Product p = new Product();
                    p.setProduct_id(rs.getInt(1));
                    p.setProduct_name(rs.getString(2));
                    p.setThumbnail(rs.getString(3));
                    p.setBase_price(rs.getBigDecimal(4));
                    p.setSold(rs.getBigDecimal(5));
                    p.setIsFreeship(rs.getInt(6));

                    Timestamp timestamp = rs.getTimestamp(7);

                    if (timestamp != null) {
                        LocalDateTime localDateTime = timestamp.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        p.setCreated_at(localDateTime);
                    }

                    p.setCoupon_id(rs.getInt(8));
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
                            new TypeReference<ArrayList<ProductDetails>>() {
                            }
                    );
                    p.setProductDetailsArrayList(productDetails);
                    list.add(p);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static ArrayList<Product> getDataByObjectName(String objName) {
        conn = dbConnect.connectionClass();
        objectMapper.registerModule(new JavaTimeModule());
        ArrayList<Product> list = new ArrayList<>();
        if (conn != null) {

            try {
                CallableStatement cstmt = conn.prepareCall("call GetProductDetailsByObjectName(?)");
                cstmt.setString(1, objName);
                ResultSet rs = cstmt.executeQuery();
                while (rs.next()) {
                    Product p = new Product();
                    p.setProduct_id(rs.getInt(1));
                    p.setProduct_name(rs.getString(2));
                    p.setThumbnail(rs.getString(3));
                    p.setBase_price(rs.getBigDecimal(4));
                    p.setSold(rs.getBigDecimal(5));
                    p.setIsFreeship(rs.getInt(6));

                    Timestamp timestamp = rs.getTimestamp(7);

                    if (timestamp != null) {
                        LocalDateTime localDateTime = timestamp.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        p.setCreated_at(localDateTime);
                    }

                    p.setCoupon_id(rs.getInt(8));
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
                            new TypeReference<ArrayList<ProductDetails>>() {
                            }
                    );
                    p.setProductDetailsArrayList(productDetails);
                    list.add(p);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static ArrayList<Product> getDataByObjectNameAndCategoryID(String objName, int catID) {
        conn = dbConnect.connectionClass();
        ArrayList<Product> list = new ArrayList<>();
        objectMapper.registerModule(new JavaTimeModule());
        if (conn != null) {

            try {
                CallableStatement cstmt = conn.prepareCall("call GetProductDetailsByObjectNameAndCategoryID(?,?)");
                cstmt.setString(1, objName);
                cstmt.setInt(2, catID);
                ResultSet rs = cstmt.executeQuery();
                while (rs.next()) {
                    Product p = new Product();
                    p.setProduct_id(rs.getInt(1));
                    p.setProduct_name(rs.getString(2));
                    p.setThumbnail(rs.getString(3));
                    p.setBase_price(rs.getBigDecimal(4));
                    p.setSold(rs.getBigDecimal(5));
                    p.setIsFreeship(rs.getInt(6));

                    Timestamp timestamp = rs.getTimestamp(7);

                    if (timestamp != null) {
                        LocalDateTime localDateTime = timestamp.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        p.setCreated_at(localDateTime);
                    }

                    p.setCoupon_id(rs.getInt(8));
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
                            new TypeReference<ArrayList<ProductDetails>>() {
                            }
                    );

                    p.setProductDetailsArrayList(productDetails);
                    list.add(p);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static Product getDataByProductID(int productID) {
        conn = dbConnect.connectionClass();
        ArrayList<Product> list = new ArrayList<>();
        objectMapper.registerModule(new JavaTimeModule());
        if (conn != null) {

            try {
                CallableStatement cstmt = conn.prepareCall("call  GetProductDetailsByProductID(?)");
                cstmt.setInt(1, productID);
                ResultSet rs = cstmt.executeQuery();
                if (rs.next()) {
                    Product p = new Product();
                    p.setProduct_id(rs.getInt(1));
                    p.setProduct_name(rs.getString(2));
                    p.setThumbnail(rs.getString(3));
                    p.setBase_price(rs.getBigDecimal(4));
                    p.setSold(rs.getBigDecimal(5));
                    p.setIsFreeship(rs.getInt(6));

                    Timestamp timestamp = rs.getTimestamp(7);

                    if (timestamp != null) {
                        LocalDateTime localDateTime = timestamp.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        p.setCreated_at(localDateTime);
                    }

                    p.setCoupon_id(rs.getInt(8));
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
                            new TypeReference<ArrayList<ProductDetails>>() {
                            }
                    );
                    p.setProductDetailsArrayList(productDetails);
                    return p;

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Product();
    }
}
