package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Cart.EmptyCart;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddAddressActivity extends AppCompatActivity {

    private ImageButton btn_return;
    private Button btnSave;
    private EditText edtStreetAddress;
    private EditText edtCity;
    private EditText edtState;
    private EditText edtZipCode;
    Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControl();
        addAddress();
        returnToListAddress();
    }

    private void addControl(){
        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        edtCity = findViewById(R.id.edtCity);
        edtState = findViewById(R.id.edtState);
        edtZipCode = findViewById(R.id.edtZipCode);
        btnSave = findViewById(R.id.btnSave);
    }

    private void addAddress(){
        String l = "5";
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBConnect c = new DBConnect();
                connection = c.connectionClass();
                try{
                    if(connection != null){
                        String sqlinsert="Insert into user_address values ('5','"+edtStreetAddress.getText().toString()+"','"+edtCity.getText().toString()+"','"+edtState.getText().toString()+"','"+edtZipCode.getText().toString()+"','null')";
                        Statement st = connection.createStatement();
                        ResultSet rs = st.executeQuery(sqlinsert);
                    }
                }catch (Exception exception){
                    Log.e("Error", exception.getMessage());
                }
                Intent myintent = new Intent(AddAddressActivity.this, ListAddressActivity.class);
                startActivity(myintent);
            }
        });
    }

    private void returnToListAddress(){
        btn_return=(ImageButton) findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}