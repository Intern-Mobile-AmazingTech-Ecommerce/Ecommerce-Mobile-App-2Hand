package com.example.ecommercemobileapp2hand.Views.Homepage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;

public class CategoryProductActivity extends AppCompatActivity {
    ImageView imgBack;
    TextView tvCategoryName;
    RecyclerView recyCategoryProduct;
    String CategoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);
        addcontrols();
        addevents();
    }
    void addcontrols()
    {
        imgBack = findViewById(R.id.imgBack);
        tvCategoryName = findViewById(R.id.tvCategoryName);
        recyCategoryProduct = findViewById(R.id.recyCategoryProduct);
    }
    void addevents()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}