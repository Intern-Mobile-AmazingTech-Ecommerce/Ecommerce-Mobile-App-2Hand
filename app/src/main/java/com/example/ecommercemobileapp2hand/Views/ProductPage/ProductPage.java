package com.example.ecommercemobileapp2hand.Views.ProductPage;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.ProductPage.Adapters.RecycleProductImageAdapter;

import java.util.ArrayList;

public class ProductPage extends AppCompatActivity {

    private RecyclerView recycleImgSlider;
    private RecycleProductImageAdapter imgSliderApdater;
    private ArrayList<String> imgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_page);
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
        loadRecycleViewImgSlider();
    }

    private void addControl(){
        recycleImgSlider = findViewById(R.id.recyclerProductImgSlider);

    }
    private void loadRecycleViewImgSlider(){
        imgList = new ArrayList<>();
        imgList.add("harringtonjacket1.png");
        imgList.add("harringtonjacket2.png");
        imgList.add("harringtonjacket1.png");
        imgList.add("harringtonjacket2.png");
        imgSliderApdater = new RecycleProductImageAdapter(imgList,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recycleImgSlider.setLayoutManager(layoutManager);
        recycleImgSlider.setAdapter(imgSliderApdater);
    }
}