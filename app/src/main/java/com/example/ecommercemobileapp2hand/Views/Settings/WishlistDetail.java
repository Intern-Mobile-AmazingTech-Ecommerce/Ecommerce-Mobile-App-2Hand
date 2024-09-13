package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.WishlistHandler;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.ProductCardAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WishlistDetail extends AppCompatActivity {
    private ExecutorService service = Executors.newCachedThreadPool();
    private ProductCardAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Product> imgList = new ArrayList<>();
    private ImageButton btn_back;
    private TextView txtWishListLabel, btn_clear, tvBlank;
    int wishList_ID;
    private UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wishlist_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIt();
        getData();
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

    private void addControls() {
        recyclerView = findViewById(R.id.recyWishListProduct);
        btn_back = findViewById(R.id.btn_back);
        txtWishListLabel = findViewById(R.id.wishlist_label);
        btn_clear = findViewById(R.id.btn_clear);
        tvBlank = findViewById(R.id.tvBlank);
    }

    private void addEvents() {
        btn_back.setOnClickListener(view ->
                finish()
        );

        btn_clear.setOnClickListener(view -> {
            service.execute(() -> {
                WishlistHandler.clearWishlist(wishList_ID);
                runOnUiThread(() -> {
                    imgList.clear();
                    adapter.notifyDataSetChanged();
                    tvBlank.setVisibility(View.VISIBLE);
                });
            });
        });
    }

    private void getData() {
        service.execute(() -> {
            imgList = WishlistHandler.getWishListDetailByWishListID(wishList_ID);
            runOnUiThread(() -> {
                if (imgList.isEmpty()) {
                    tvBlank.setVisibility(View.VISIBLE);
                } else {
                    tvBlank.setVisibility(View.GONE);
                }
                adapter = new ProductCardAdapter(imgList, WishlistDetail.this, new ProductCardAdapter.FavoriteClickedListener() {
                    @Override
                    public void onDoneClicked() {

                    }
                },wishList_ID );
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(WishlistDetail.this, 2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    private void getIt() {
        Intent intent = getIntent();
        wishList_ID = intent.getIntExtra("wishlistID", 0);
//        userAccount = (UserAccount) intent.getSerializableExtra("UserAccount");
    }
}
