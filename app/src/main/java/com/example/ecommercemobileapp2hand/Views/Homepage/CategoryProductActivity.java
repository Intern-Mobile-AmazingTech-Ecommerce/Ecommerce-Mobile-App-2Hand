package com.example.ecommercemobileapp2hand.Views.Homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.ProductHandler;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.ProductCardAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CategoryProductActivity extends AppCompatActivity {
    private ExecutorService service = Executors.newCachedThreadPool();
    private CountDownLatch latch = new CountDownLatch(1);
    private ProductCategory Category;
    private ImageView imgBack;
    private TextView tvCategoryName;
    private RecyclerView recyCategoryProduct;
    private ArrayList<Product> lstPro;
    private ProductCardAdapter proAdapter;
    private ArrayList<Product> topSellingList;
    private ArrayList<Product> newInList;
    private String gender;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getBundleIntent();
        getGenderKey();
        addcontrols();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecycleCategoryProduct();
        addEvents();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (service != null && !service.isShutdown()) {
            service.shutdown();
            try {
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void getBundleIntent() {
        Intent intent = getIntent();
//        Category = new ProductCategory();
        topSellingList = new ArrayList<>();
        newInList = new ArrayList<>();
        if (intent.getParcelableExtra("Category") != null) {
            Category = intent.getParcelableExtra("Category");
//            genderTextView = intent.getStringExtra("Gender");
        } else if (intent.getParcelableArrayListExtra("TopSellingList") != null) {
            topSellingList = intent.getParcelableArrayListExtra("TopSellingList");
        } else if (intent.getParcelableArrayListExtra("NewInList") != null) {
            newInList = intent.getParcelableArrayListExtra("NewInList");
        }
//        latch.countDown();
    }


    private void addcontrols() {
        imgBack = findViewById(R.id.imgBack);
        tvCategoryName = findViewById(R.id.tvCategoryName);
        recyCategoryProduct = findViewById(R.id.recyCategoryProduct);
    }

    private void loadRecycleCategoryProduct() {

        if (Category != null) {

            ProductHandler.getDataByObjectNameAndCategoryID(gender, Category.getProduct_category_id(), new ProductHandler.Callback<ArrayList<Product>>() {
                @Override
                public void onResult(ArrayList<Product> result) {
                    runOnUiThread(() -> {
                        lstPro = result;
                        if (lstPro != null && !lstPro.isEmpty()) {
                            proAdapter = new ProductCardAdapter(lstPro, CategoryProductActivity.this);
                            tvCategoryName.setText(Category.getProduct_category_name() + " (" + lstPro.size() + ")");
                            recyCategoryProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            recyCategoryProduct.setItemAnimator(new DefaultItemAnimator());
                            recyCategoryProduct.setAdapter(proAdapter);
                        }

                    });
                }
            });

        } else if (topSellingList.size() > 0) {
            proAdapter = new ProductCardAdapter(topSellingList, CategoryProductActivity.this);
            tvCategoryName.setText("Top Selling (" + topSellingList.size() + ")");
            recyCategoryProduct.setAdapter(proAdapter);

        } else if (newInList.size() > 0) {

            proAdapter = new ProductCardAdapter(newInList, CategoryProductActivity.this);
            tvCategoryName.setText("New In (" + newInList.size() + ")");
            recyCategoryProduct.setAdapter(proAdapter);
        }
        recyCategoryProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyCategoryProduct.setItemAnimator(new DefaultItemAnimator());
//        recyCategoryProduct.setAdapter(proAdapter);

    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getGenderKey() {
        sharedPreferences = getSharedPreferences("my_userID", Context.MODE_PRIVATE);
        gender = sharedPreferences.getString("gender_key", "");
        if (gender.isEmpty()) {
            gender = "Men";
        }
    }
}