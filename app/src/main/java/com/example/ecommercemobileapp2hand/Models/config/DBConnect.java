package com.example.ecommercemobileapp2hand.Models.config;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private Connection conn = null;
    private String username, password, serverName, port, database;

    public Connection connectionClass() {
        serverName = "KHANH";
        database = "Ecommerce2Hand";
        username = "sa";
        password = "123";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String connectionStr = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connectionStr = "jdbc:sqlserver://" + serverName + ":" + port + ";databaseName=" + database;
            conn = DriverManager.getConnection(connectionStr, username, password);

            Log.i("DBConnect", "Kết nối thành công");

        } catch (ClassNotFoundException e) {
            Log.e("DBConnect", "Driver không tìm thấy: " + e.getMessage());
        } catch (SQLException e) {
            Log.e("DBConnect", "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        } catch (Exception e) {
            Log.e("DBConnect", "Lỗi: " + e.getMessage());
        }
        return conn;
    }

}