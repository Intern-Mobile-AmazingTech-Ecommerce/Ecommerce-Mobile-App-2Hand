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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.ProductHandler;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.ProductCardAdapter;

import java.util.ArrayList;

public class CategoryProductActivity extends AppCompatActivity {
    ImageView imgBack;
    TextView tvCategoryName;
    RecyclerView recyCategoryProduct;
    ArrayList<Product> lstPro;
    ProductCardAdapter proAdapter;
    String CategoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);
        addcontrols();
        addevents();
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void addcontrols()
    {
        imgBack = findViewById(R.id.imgBack);
        tvCategoryName = findViewById(R.id.tvCategoryName);
        recyCategoryProduct = findViewById(R.id.recyCategoryProduct);

        lstPro = ProductHandler.getDataByObjectName("Men");

        proAdapter = new ProductCardAdapter(lstPro, CategoryProductActivity.this);

        recyCategoryProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyCategoryProduct.setItemAnimator(new DefaultItemAnimator());

        recyCategoryProduct.setAdapter(proAdapter);
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