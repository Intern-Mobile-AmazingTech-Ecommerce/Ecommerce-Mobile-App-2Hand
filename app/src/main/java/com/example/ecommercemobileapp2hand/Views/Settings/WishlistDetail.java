package com.example.ecommercemobileapp2hand.Views.Settings;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.WishlistHandler;
import com.example.ecommercemobileapp2hand.Models.Product;
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
        addEvents();
    }
    private void addControls(){
        recyclerView=findViewById(R.id.recyWishListProduct);
        btn_back=findViewById(R.id.btn_back);
        txtWishListLabel=findViewById(R.id.wishlist_label);
    }
    private void addEvents(){
        adapter=new ProductCardAdapter(imgList,WishlistDetail.this);
    }
    private void getData(){
        imgList= WishlistHandler.getData();
    }
}