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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.WishlistHandler;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.ProductCardAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleProductImageAdapter;

import java.util.ArrayList;

public class WishlistDetail extends AppCompatActivity {
    ProductCardAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Product> imgList=new ArrayList<>();
    ImageButton btn_back;
    TextView txtWishListLabel;
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
        getIt();
        addControls();
        getData();
        addEvents();
    }
    private void addControls(){
        recyclerView=findViewById(R.id.recyWishListProduct);
        btn_back=findViewById(R.id.btn_back);
        txtWishListLabel=findViewById(R.id.wishlist_label);
    }
    private void addEvents(){
        adapter=new ProductCardAdapter(imgList,WishlistDetail.this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void getData(){
        imgList= WishlistHandler.getWishListDetailByWishListID(wishList_ID);
        adapter=new ProductCardAdapter(imgList,WishlistDetail.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(WishlistDetail.this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void getIt()
    {
        Intent intent=getIntent();
        wishList_ID = intent.getIntExtra("wishlistID",0);
        userAccount = (UserAccount) intent.getSerializableExtra("UserAccount");
    }
}