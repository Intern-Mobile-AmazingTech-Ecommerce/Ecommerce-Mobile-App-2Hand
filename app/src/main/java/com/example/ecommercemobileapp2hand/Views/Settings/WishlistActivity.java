package com.example.ecommercemobileapp2hand.Views.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.FakeModels.WishList;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.WishListAdapter;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {
    RecyclerView rv_wishlist;
    ImageView btnBack;
    private WishListAdapter wishListAdapter;
    private List<WishList> wishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wishlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        addEvent();
        loadRecycleViewCategories();
    }
    private void addControl(){
        rv_wishlist = findViewById(R.id.rv_wishlist);
        rv_wishlist.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
    }
    private void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadRecycleViewCategories(){
        // Initialize category list
        wishList= new ArrayList<>();
        wishList.add(new WishList("My Favorite", 12));
        wishList.add(new WishList("T-Shirts", 4));


        wishListAdapter = new WishListAdapter(WishlistActivity.this,wishList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WishlistActivity.this,RecyclerView.VERTICAL,false);
        rv_wishlist.setLayoutManager(layoutManager);

        rv_wishlist.setAdapter(wishListAdapter);
    }
}