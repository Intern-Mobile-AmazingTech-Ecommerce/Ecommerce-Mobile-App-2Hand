package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
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

import com.example.ecommercemobileapp2hand.Controllers.WishlistHandler;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.Wishlist;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.WishListAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WishlistActivity extends AppCompatActivity {
    private ExecutorService service = Executors.newCachedThreadPool();
    RecyclerView rv_wishlist;
    ImageView btnBack;
    private WishListAdapter wishListAdapter;
    private List<Wishlist> wishList;
    private UserAccount userAccount;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIt();
        addEvent();
        loadRecycleViewCategories();
    }
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
        service.execute(()->{
            wishList = WishlistHandler.getWishListByUserID(1);
            runOnUiThread(()->{
                wishListAdapter = new WishListAdapter(WishlistActivity.this, wishList, userAccount);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WishlistActivity.this,RecyclerView.VERTICAL,false);
                rv_wishlist.setLayoutManager(layoutManager);

                rv_wishlist.setAdapter(wishListAdapter);
            });
        });

    }
    private void getIt()
    {
        Intent intent = getIntent();
        userAccount = (UserAccount) intent.getSerializableExtra("UserAccount");
    }
}