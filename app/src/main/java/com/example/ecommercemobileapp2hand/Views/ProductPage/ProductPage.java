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
import android.widget.Button;
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
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleProductImageAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleReviewAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecylerColorAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProductPage extends AppCompatActivity {

    private Product product;
    private ProductDetails currentDetails;
    private RecyclerView recycleImgSlider;
    private RecycleProductImageAdapter imgSliderApdater;
    private ArrayList<ProductDetailsImg> imgList;
    private ArrayList<Reviews> reviewsList;
    private ArrayList<ProductColor> colorList;
    private RecycleReviewAdapter reviewAdapter;
    private RecyclerView recycleReviews;
    RelativeLayout btnColor, btnSize;
    private ImageView imgBack;
    private TextView tvProductName, tvPrice, tvOldPrice, tvDescription;
    private View bgColor;

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
        String colorName = curr.getProductColor().getProduct_color_name().toLowerCase();
        int color;
        if (colorName == "dark blue") {
            color = Color.parseColor("#00008B");
        } else {
            color = Color.parseColor(colorName);
        }
        bgColor.setBackgroundTintList(ColorStateList.valueOf(color));
        tvDescription.setText(curr.getDescription());
    }

    private void addControl() {

        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvOldPrice = findViewById(R.id.tvOldPrice);
        tvDescription = findViewById(R.id.tvDescription);
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
    }

    public void addEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
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
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.color_overlay, null);
//        bottomSheetDialog.setContentView(dialogView);
//        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
//        overlayTitle.setText(type);
//        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
//        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
//
//        RecyclerView recyclerColor = dialogView.findViewById(R.id.recyclerColor);
//        RecylerColorAdapter  recylerColorAdapter = new RecylerColorAdapter(colorList,bottomSheetDialog.getContext(),);
////        String[] colors = {"Red", "Yellow", "Blue", "Black"};
////        int[] colorValues = {Color.parseColor("#FA3636"), Color.parseColor("#F4BD2F"), Color.parseColor("#4468E5"), Color.parseColor("#272727")};
////
////        //array để lưu các button
////        Button[] buttons = new Button[colors.length];
////        for (int i = 0; i < colors.length; i++) {
////            //view cho các button
////            View buttonView = inflater.inflate(R.layout.color_button, null);
////            Button button = buttonView.findViewById(R.id.button_content);
////            ImageView iconCheck = buttonView.findViewById(R.id.icon_check);
////            button.setText(colors[i]);
////            iconCheck.setVisibility(View.GONE); // ẩn check
////            View colorView = buttonView.findViewById(R.id.color);
////            colorView.setBackgroundTintList(ColorStateList.valueOf(colorValues[i]));
////            button.setOnClickListener(v -> {
////                // update các button chưa chọn
////                for (int j = 0; j < linearLayout.getChildCount(); j++) {
////                    View child = linearLayout.getChildAt(j);
////                    Button btn = child.findViewById(R.id.button_content);
////                    ImageView checkIcon = child.findViewById(R.id.icon_check);
////                    checkIcon.setVisibility(View.GONE);
////                }
////
////                // update button được chọn
////                button.setBackgroundColor(getResources().getColor(R.color.purple));
////                button.setTextColor(getResources().getColor(R.color.white));
////                iconCheck.setVisibility(View.VISIBLE);
////            });
////
////            // Thêm button vào LinearLayout
////            linearLayout.addView(buttonView);
////            buttons[i] = button;
////        }
////
////        //button đầu là dèault
////        if (linearLayout.getChildCount() > 0) {
////            View defaultButtonView = linearLayout.getChildAt(0);
////            Button defaultButton = defaultButtonView.findViewById(R.id.button_content);
////            ImageView defaultIconCheck = defaultButtonView.findViewById(R.id.icon_check);
////            defaultButton.setBackgroundColor(getResources().getColor(R.color.purple));
////            defaultButton.setTextColor(getResources().getColor(R.color.white));
////            defaultIconCheck.setVisibility(View.VISIBLE);
////        }
//        bottomSheetDialog.show();
    }

    private void showSizeOverlay(String type) {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.size_overlay, null);
//        bottomSheetDialog.setContentView(dialogView);
//
//        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
//        overlayTitle.setText(type);
//
//        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//        LinearLayout linearLayout = dialogView.findViewById(R.id.overlay_sub_button);
//        String[] sizes = {"S", "M", "L", "XL", "2XL"};
//
//        Button[] buttons = new Button[sizes.length];
//        for (int i = 0; i < sizes.length; i++) {
//            View buttonView = inflater.inflate(R.layout.custom_button, null);
//            Button button = buttonView.findViewById(R.id.button_content);
//            ImageView iconCheck = buttonView.findViewById(R.id.icon_check);
//            button.setText(sizes[i]);
//            button.setBackgroundColor(getResources().getColor(R.color.Bg_Light_2));
//            button.setTextColor(getResources().getColor(R.color.black));
//            iconCheck.setVisibility(View.GONE);
//
//            button.setOnClickListener(v -> {
//                for (int j = 0; j < linearLayout.getChildCount(); j++) {
//                    View child = linearLayout.getChildAt(j);
//                    Button btn = child.findViewById(R.id.button_content);
//                    ImageView checkIcon = child.findViewById(R.id.icon_check);
//
//                    btn.setBackgroundColor(getResources().getColor(R.color.Bg_Light_2));
//                    btn.setTextColor(getResources().getColor(R.color.black));
//                    checkIcon.setVisibility(View.GONE);
//                }
//                button.setBackgroundColor(getResources().getColor(R.color.purple));
//                button.setTextColor(getResources().getColor(R.color.white));
//                iconCheck.setVisibility(View.VISIBLE);
//            });
//            linearLayout.addView(buttonView);
//            buttons[i] = button;
//        }
//
//        if (linearLayout.getChildCount() > 0) {
//            View defaultButtonView = linearLayout.getChildAt(0);
//            Button defaultButton = defaultButtonView.findViewById(R.id.button_content);
//            ImageView defaultIconCheck = defaultButtonView.findViewById(R.id.icon_check);
//            defaultButton.setBackgroundColor(getResources().getColor(R.color.purple));
//            defaultButton.setTextColor(getResources().getColor(R.color.white));
//            defaultIconCheck.setVisibility(View.VISIBLE);
//        }
//        bottomSheetDialog.show();
    }

    private void addButtons(LinearLayout linearLayout, String[] options, BottomSheetDialog bottomSheetDialog) {
        for (String option : options) {
            Button button = new Button(this);
            button.setText(option);
            button.setOnClickListener(v -> {
                Toast.makeText(ProductPage.this, "Selected: " + option, Toast.LENGTH_SHORT).show();
            });
            linearLayout.addView(button);
        }
    }

}