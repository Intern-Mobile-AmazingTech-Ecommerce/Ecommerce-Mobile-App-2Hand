package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddCardActivity extends AppCompatActivity {

    private ImageView imgBack;
    private EditText edtCardNumber, edtCCV, edtEXP, edtCardholderName;
    private Button btnSaveCard;
    Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addCard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvents();
    }

    private void addControls()
    {
        imgBack = findViewById(R.id.imgBack);
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtCCV = findViewById(R.id.edtCCV);
        edtEXP = findViewById(R.id.edtEXP);
        edtCardholderName = findViewById(R.id.edtCardholderName);
        btnSaveCard = findViewById(R.id.btnSaveCard);
    }

    private void addCard(){
        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBConnect c = new DBConnect();
                connection = c.connectionClass();
                try{
                    if(connection != null){
                        String sqlinsert="Insert into user_cards values ('5','"+edtCardNumber.getText().toString()+"','"+edtCCV.getText().toString()+"','"+edtEXP.getText().toString()+"','"+edtCardholderName.getText().toString()+"')";
                        Statement st = connection.createStatement();
                        ResultSet rs = st.executeQuery(sqlinsert);
                    }
                }catch (Exception exception){
                    Log.e("Error", exception.getMessage());
                }
                Intent myintent = new Intent(AddCardActivity.this, PaymentActivity.class);
                startActivity(myintent);
            }
        });
    }


    private void addEvents()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}