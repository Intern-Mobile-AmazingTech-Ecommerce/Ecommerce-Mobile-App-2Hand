package com.example.ecommercemobileapp2hand.Views.ProductPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.FakeModels.Reviews;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductColor;
import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Models.ProductDetailsImg;
import com.example.ecommercemobileapp2hand.Models.ProductDetailsSize;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleProductImageAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleReviewAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleSizeAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecylerColorAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.SortByAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProductPage extends AppCompatActivity {

    private Product product;
    private ProductDetails currentDetails;
    private ProductDetailsSize currentSize;
    private RecyclerView recycleImgSlider;
    private RecycleProductImageAdapter imgSliderApdater;
    private ArrayList<ProductDetailsImg> imgList;
    private ArrayList<Reviews> reviewsList;
    private ArrayList<ProductColor> colorList;
    private RecycleReviewAdapter reviewAdapter;
    private RecyclerView recycleReviews;
    private RelativeLayout btnColor, btnSize;
    private ImageView imgBack, btnIncrease, btnDecrease;
    private TextView tvProductName, tvPrice, tvOldPrice, tvDescription, tvSize, tvQuantity,tvTotalPrice;
    private View bgColor;
    private int quantity = 1;
    private BigDecimal totalPrice;
    private RelativeLayout btnAddToBag;
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
        getBundleIntent();

    }

    private void getBundleIntent() {
        Intent intent = getIntent();
        if (intent.getParcelableExtra("lstDetails") != null) {
            product = intent.getParcelableExtra("lstDetails");
        }
        if (intent.getParcelableExtra("currentSale") != null) {
            currentDetails = intent.getParcelableExtra("currentSale");
        } else {
            currentDetails = product.getProductDetailsArrayList().get(0);
        }
        currentSize = currentDetails.getSizeArrayList().get(0);
        colorList = product.getProductDetailsArrayList().stream().map(productDetails -> productDetails.getProductColor()).distinct().collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadListViewReviews();
        addEvent();
        bindingData(currentDetails);
    }

    @SuppressLint("ResourceType")
    private void bindingData(ProductDetails curr) {
        loadRecycleViewImgSlider(curr);
        tvProductName.setText(product.getProduct_name());
        if (curr.getSale_price() != null) {
            tvOldPrice.setVisibility(View.VISIBLE);
            tvOldPrice.setText("$" + product.getBase_price().toString());
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPrice.setText("$" + curr.getSale_price().toString());
        } else {
            tvOldPrice.setVisibility(View.GONE);
            tvPrice.setText(product.getBase_price().toString());

        }
        String colorName = curr.getProductColor().getProduct_color_name().toLowerCase().trim();
        int color;
        if (colorName.contains("dark blue")) {
            color = Color.parseColor("#00008B");
        } else {
            color = Color.parseColor(colorName);
        }
        bgColor.setBackgroundTintList(ColorStateList.valueOf(color));
        tvDescription.setText(curr.getDescription());

        quantity = 1;
        tvQuantity.setText(String.valueOf(quantity));

        if(curr.getSale_price()!=null){
            tvTotalPrice.setText(String.valueOf(currentDetails.getSale_price().toString()));
            totalPrice = currentDetails.getSale_price();
        }else {
            tvTotalPrice.setText(String.valueOf(product.getBase_price().toString()));
            totalPrice = product.getBase_price();
        }
    }

    private void addControl() {

        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvOldPrice = findViewById(R.id.tvOldPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvSize = findViewById(R.id.txtSize);
        recycleImgSlider = findViewById(R.id.recyclerProductImgSlider);
        recycleReviews = findViewById(R.id.recyclerRating);
        btnColor = findViewById(R.id.btnColor);
        btnSize = findViewById(R.id.btnSize);
        bgColor = findViewById(R.id.bgColor);
        if (btnColor != null) {
            btnColor.setOnClickListener(v -> showColorOverlay("Color"));
        }
        imgBack = findViewById(R.id.imgBack);
        if (btnSize != null) {
            btnSize.setOnClickListener(v -> showSizeOverlay("Size"));
        }


        //Quantity
        btnIncrease = findViewById(R.id.btnIncreaseQuantity);
        btnDecrease = findViewById(R.id.btnDecreaseQuantity);
        tvQuantity = findViewById(R.id.txtQuantityValue);

        //btnAddToBag
        btnAddToBag = findViewById(R.id.btnAddToBag);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
    }

    public void addEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity+=1;
                tvQuantity.setText(String.valueOf(quantity));
                totalPrice = totalPrice.multiply(BigDecimal.valueOf(quantity));
                tvTotalPrice.setText(totalPrice.toString());
            }
        });
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity != 1) {
                    quantity-=1;
                    tvQuantity.setText(String.valueOf(quantity));
                    totalPrice = totalPrice.multiply(BigDecimal.valueOf(quantity));
                    tvTotalPrice.setText(totalPrice.toString());

                }


            }
        });
    }

    private void loadRecycleViewImgSlider(ProductDetails productDetails) {
        imgList = productDetails.getImgDetailsArrayList();
        imgSliderApdater = new RecycleProductImageAdapter(imgList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycleImgSlider.setLayoutManager(layoutManager);
        recycleImgSlider.setAdapter(imgSliderApdater);
    }

    private void loadListViewReviews() {
        reviewsList = new ArrayList<>();
        reviewsList.add(new Reviews(1, "Alex Morgan", "AlexMorgan.png", 3, "Gucci transcribes its heritage, creativity, and innovation into a plenitude of collections. From staple items to distinctive accessories."));
        reviewsList.add(new Reviews(2, "Alex Morgan", "AlexMorgan.png", 3, "Gucci transcribes its heritage, creativity, and innovation into a plenitude of collections. From staple items to distinctive accessories."));
        reviewsList.add(new Reviews(3, "Alex Morgan", "AlexMorgan.png", 3, "Gucci transcribes its heritage, creativity, and innovation into a plenitude of collections. From staple items to distinctive accessories."));
        reviewAdapter = new RecycleReviewAdapter(reviewsList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycleReviews.setLayoutManager(layoutManager);
        recycleReviews.setAdapter(reviewAdapter);
    }

    private void showColorOverlay(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.color_overlay, null);
        bottomSheetDialog.setContentView(dialogView);
        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText(type);
        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        RecyclerView recyclerColor = dialogView.findViewById(R.id.recyclerColor);
        RecylerColorAdapter recylerColorAdapter = new RecylerColorAdapter(colorList, ProductPage.this, currentDetails.getProductColor().getProduct_color_name(), new RecylerColorAdapter.OnColorsSelectedListener() {
            @Override
            public void onColorSelected(String colorName) {
                currentDetails = product.getProductDetailsArrayList().stream()
                        .filter(productDetails -> productDetails.getProductColor().getProduct_color_name().equalsIgnoreCase(colorName)) // Lọc theo tên màu
                        .findFirst()
                        .orElse(null);
                bindingData(currentDetails);
                bottomSheetDialog.dismiss();
            }
        });
        recyclerColor.setLayoutManager(new LinearLayoutManager(bottomSheetDialog.getContext(), RecyclerView.VERTICAL, false));
        recyclerColor.setAdapter(recylerColorAdapter);
        bottomSheetDialog.show();
    }

    private void showSizeOverlay(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.size_overlay, null);
        bottomSheetDialog.setContentView(dialogView);

        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText(type);

        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        RecyclerView recyclerSize = dialogView.findViewById(R.id.recy_size);
        RecycleSizeAdapter recycleSizeAdapter = new RecycleSizeAdapter(currentDetails.getSizeArrayList(), ProductPage.this, new RecycleSizeAdapter.OnSizeSelectedListener() {
            @Override
            public void onSizeSelected(ProductDetailsSize size) {
                currentSize = size;
                tvSize.setText(currentSize.getSize().getSize_name());
                bottomSheetDialog.dismiss();
            }
        }, currentSize);
        recyclerSize.setLayoutManager(new LinearLayoutManager(bottomSheetDialog.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerSize.setAdapter(recycleSizeAdapter);
        bottomSheetDialog.show();

    }


}