package com.example.ecommercemobileapp2hand.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnect extends AppCompatActivity {
    Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_connect);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvResult = findViewById(R.id.tvResult);
        Button btnGetData = findViewById(R.id.btnGetData);

        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBConnect dbConnection = new DBConnect();
                connection = dbConnection.connectionClass();
                if(dbConnection!= null){
                    String query = "Select * from product";
                    try {
                        Statement smt = connection.createStatement();
                        ResultSet set = smt.executeQuery(query);
                        while (set.next()){
                            tvResult.setText(set.getString(2));
                        }
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }
}