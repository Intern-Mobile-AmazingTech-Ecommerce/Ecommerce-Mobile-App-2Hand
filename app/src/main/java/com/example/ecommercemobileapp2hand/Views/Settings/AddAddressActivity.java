package com.example.ecommercemobileapp2hand.Views.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;

public class AddAddressActivity extends AppCompatActivity {

    private ImageView imgBack;
    private EditText edtStreetAddress, edtCity, edtState, edtZipCode;
    private Button btnSaveAddress;
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
        addControls();
        addEvents();
    }
    private void addControls()
    {
        imgBack = findViewById(R.id.imgBack);
        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        edtCity = findViewById(R.id.edtCity);
        edtState= findViewById(R.id.edtState);
        edtZipCode= findViewById(R.id.edtZipCode);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
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