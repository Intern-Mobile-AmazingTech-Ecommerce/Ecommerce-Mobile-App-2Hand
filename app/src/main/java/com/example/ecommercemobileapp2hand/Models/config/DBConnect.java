package com.example.ecommercemobileapp2hand.Models.config;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    String username,password,  ip, port, database;
    public Connection connectionClass() {
        ip = "10.0.2.2";
        database = "Ecommerce2Hand";
        username = "sa";
        password = "123456";
        port = "1433";
        Connection conn = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String ConnectionStr = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionStr = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databaseName=" + database + ";user=" + username + ";password=" + password + ";encrypt=true;trustServerCertificate=true;";
            conn = DriverManager.getConnection(ConnectionStr);
            System.out.println("Ket noi duoc roi ne");

        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
        return conn;
    }
}