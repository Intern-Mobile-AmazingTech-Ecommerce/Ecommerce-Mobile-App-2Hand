package com.example.ecommercemobileapp2hand.Views.Homepage;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ecommercemobileapp2hand.Models.Category;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Homepage.CustomAdapter.CategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {


    private RecyclerView recyclerViewCategories;
    private CategoriesAdapter categoriesAdapter;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        addControl();
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecycleViewCategories();
        addEvent();

    }
    private void addControl(){
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
    }
    private void addEvent(){
        // Back button listener
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
    private void loadRecycleViewCategories(){
        // Initialize category list
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Hoodies", R.drawable.ic_hoodies));
        categoryList.add(new Category("Accessories", R.drawable.ic_accessories));
        categoryList.add(new Category("Shorts", R.drawable.ic_shorts));
        categoryList.add(new Category("Shoes", R.drawable.ic_shoes));
        categoryList.add(new Category("Bags", R.drawable.ic_bags));

        categoriesAdapter = new CategoriesAdapter(categoryList, this,R.layout.item_category);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerViewCategories.setLayoutManager(layoutManager);
        recyclerViewCategories.setAdapter(categoriesAdapter);
    }
}