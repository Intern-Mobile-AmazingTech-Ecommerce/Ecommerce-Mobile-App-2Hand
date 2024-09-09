package com.example.ecommercemobileapp2hand.Views.Settings;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddCardActivity extends AppCompatActivity {
    EditText card_number, CCV, exp, cardholder_name;
    Button btnSave;
    ImageButton btnBack;
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

        addControl();
        addCard();
    }
    private void addControl(){
        card_number = findViewById(R.id.card_number);
        CCV = findViewById(R.id.CCV);
        exp = findViewById(R.id.exp);
        cardholder_name = findViewById(R.id.cardholder_name);
        btnSave = findViewById(R.id.btnSave);
    }

    private void addCard(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBConnect c = new DBConnect();
                connection = c.connectionClass();
                try{
                    if(connection != null){
                        String sqlinsert="Insert into user_cards values ('5','"+card_number.getText().toString()+"','"+CCV.getText().toString()+"','"+exp.getText().toString()+"','"+cardholder_name.getText().toString()+"')";
                        Statement st = connection.createStatement();
                        ResultSet rs = st.executeQuery(sqlinsert);
                    }
                }catch (Exception exception){
                    Log.e("Error", exception.getMessage());
                }
            }
        });
    }
}