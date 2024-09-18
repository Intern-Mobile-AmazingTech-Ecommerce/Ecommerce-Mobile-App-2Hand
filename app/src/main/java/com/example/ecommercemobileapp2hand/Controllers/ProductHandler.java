package com.example.ecommercemobileapp2hand.Controllers;

import android.util.Log;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProductHandler {
    private static DBConnect dbConnect = new DBConnect();
    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static void getData(Callback<ArrayList<Product>> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {

            Connection conn = dbConnect.connectionClass();
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
                        p.setIsFreeship(rs.getBoolean(6));

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
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            callback.onResult(list);
            shutDownExecutor(service);
        });

    }

    public static void getDataByObjectName(String objName, Callback<ArrayList<Product>> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            Connection conn = dbConnect.connectionClass();
            ArrayList<Product> list = new ArrayList<>();
            if (conn != null) {
                try {
                    Log.d("getDataByObjectName", "Connection established");
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
                        p.setIsFreeship(rs.getBoolean(6));

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
                    Log.d("getDataByObjectName", "Products found: " + list.size());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (conn != null){
                            conn.close();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            callback.onResult(list);
            shutDownExecutor(service);
        });

    }

    public static void getDataByObjectNameAndCategoryID(String objName, int catID, Callback<ArrayList<Product>> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            Connection conn = dbConnect.connectionClass();
            ArrayList<Product> list = new ArrayList<>();
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
                        p.setIsFreeship(rs.getBoolean(6));

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
            callback.onResult(list);
            shutDownExecutor(service);
        });

    }

    public static void getDataByProductID(int productID, Callback<Product> callback) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            Connection conn = dbConnect.connectionClass();
            if (conn != null) {
                try {
                    CallableStatement cstmt = conn.prepareCall("call GetProductDetailsByProductID(?)");
                    cstmt.setInt(1, productID);
                    ResultSet rs = cstmt.executeQuery();
                    if (rs.next()) {
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

                        p.setCoupon_id(rs.getInt(8));
                        // Object
                        ProductObject obj = new ProductObject();
                        obj.setProduct_object_id(rs.getInt(9));
                        obj.setObject_name(rs.getString(10));
                        p.setProductObject(obj);
                        // Category
                        ProductCategory category = new ProductCategory();
                        category.setProduct_category_id(rs.getInt(11));
                        category.setProduct_category_name(rs.getString(12));
                        category.setProduct_category_description(rs.getString(13));
                        category.setProduct_category_thumbnail(rs.getString(14));
                        p.setProductCategory(category);

                        // Array Pro Details
                        String productDetailsJson = rs.getString("product_details_array");
                        ArrayList<ProductDetails> productDetails = objectMapper.readValue(
                                productDetailsJson,
                                new TypeReference<ArrayList<ProductDetails>>() {
                                }
                        );
                        p.setProductDetailsArrayList(productDetails);
                        callback.onResult(p);
                    }
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (SQLException | JsonProcessingException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
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
