package com.example.ecommercemobileapp2hand.Views.ProductPage;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.Reviews;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.ProductPage.Adapters.RecycleProductImageAdapter;
import com.example.ecommercemobileapp2hand.Views.ProductPage.Adapters.RecycleReviewAdapter;

import java.util.ArrayList;

public class ProductPage extends AppCompatActivity {

    private RecyclerView recycleImgSlider;
    private RecycleProductImageAdapter imgSliderApdater;
    private ArrayList<String> imgList;
    private ArrayList<Reviews> reviewsList;
    private RecycleReviewAdapter reviewAdapter;
    private RecyclerView recycleReviews;
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
        loadListViewReviews();
    }

    private void addControl(){
        recycleImgSlider = findViewById(R.id.recyclerProductImgSlider);
        recycleReviews = findViewById(R.id.recyclerRating);

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
    private void loadListViewReviews(){
        reviewsList = new ArrayList<>();
        reviewsList.add(new Reviews(1,"Alex Morgan","AlexMorgan.png",3,"Gucci transcribes its heritage, creativity, and innovation into a plenitude of collections. From staple items to distinctive accessories."));
        reviewsList.add(new Reviews(2,"Alex Morgan","AlexMorgan.png",3,"Gucci transcribes its heritage, creativity, and innovation into a plenitude of collections. From staple items to distinctive accessories."));
        reviewsList.add(new Reviews(3,"Alex Morgan","AlexMorgan.png",3,"Gucci transcribes its heritage, creativity, and innovation into a plenitude of collections. From staple items to distinctive accessories."));
        reviewAdapter = new RecycleReviewAdapter(reviewsList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recycleReviews.setLayoutManager(layoutManager);
        recycleReviews.setAdapter(reviewAdapter);
    }
}