package com.example.ecommercemobileapp2hand.Views.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;

public class AddCardActivity extends AppCompatActivity {

    private ImageView imgBack;
    private EditText edtCardNumber, edtCCV, edtEXP, edtCardholderName;
    private Button btnSaveCard;
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