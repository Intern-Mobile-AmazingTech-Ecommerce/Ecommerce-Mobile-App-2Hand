package com.example.ecommercemobileapp2hand.Views.Homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ecommercemobileapp2hand.R;

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

        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));

        // Initialize category list
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Hoodies", R.drawable.ic_hoodies));
        categoryList.add(new Category("Accessories", R.drawable.ic_accessories));
        categoryList.add(new Category("Shorts", R.drawable.ic_shorts));
        categoryList.add(new Category("Shoes", R.drawable.ic_shoes));
        categoryList.add(new Category("Bags", R.drawable.ic_bags));

        categoriesAdapter = new CategoriesAdapter(categoryList, this);
        recyclerViewCategories.setAdapter(categoriesAdapter);

        // Back button listener
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}