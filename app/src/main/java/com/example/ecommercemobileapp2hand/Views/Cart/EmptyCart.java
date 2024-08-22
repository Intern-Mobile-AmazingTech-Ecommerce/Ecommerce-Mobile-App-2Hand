package com.example.ecommercemobileapp2hand.Views.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Login.SignInActivity;
import com.example.ecommercemobileapp2hand.Views.Login.returnlogin;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

public class EmptyCart extends AppCompatActivity {
    Button btnExplore_Categories_Cart;
    ImageButton btnBack1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empty_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnExplore_Categories_Cart = findViewById(R.id.btnExplore_Categories_Cart);
        btnBack1 = findViewById(R.id.btnBack1);
        //Nut Explore_Categories_Cart
        btnExplore_Categories_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(EmptyCart.this, MainActivity.class);
                startActivity(myintent);
            }
        });
        // Nut quay ve
        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(EmptyCart.this, MainActivity.class);
                startActivity(myintent);
            }
        });
    }
}