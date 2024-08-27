package com.example.ecommercemobileapp2hand.Views.ProductPage;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ProductPage extends AppCompatActivity {

    private RecyclerView recycleImgSlider;
    private RecycleProductImageAdapter imgSliderApdater;
    private ArrayList<String> imgList;
    private ArrayList<Reviews> reviewsList;
    private RecycleReviewAdapter reviewAdapter;
    private RecyclerView recycleReviews;
    ImageView btnColor, btnSize;
    private ImageView imgBack;
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
        addEvent();
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
        btnColor = findViewById(R.id.btnColor);
        btnSize = findViewById(R.id.btnSize);

        if (btnColor != null) {
            btnColor.setOnClickListener(v -> showColorOverlay("Color"));
        }
        imgBack = findViewById(R.id.imgBack);

        if (btnSize != null) {
            btnSize.setOnClickListener(v -> showSizeOverlay("Size"));
        }
    }
    public void addEvent()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
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

    private void showColorOverlay(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.color_overlay, null);
        bottomSheetDialog.setContentView(dialogView);
        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText(type);
        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        LinearLayout linearLayout = dialogView.findViewById(R.id.overlay_sub_button);
        String[] colors = {"Red", "Yellow", "Blue", "Black"};
        int[] colorValues = {Color.parseColor("#FA3636"), Color.parseColor("#F4BD2F"), Color.parseColor("#4468E5"), Color.parseColor("#272727")};

        //array để lưu các button
        Button[] buttons = new Button[colors.length];
        for (int i = 0; i < colors.length; i++) {
            //view cho các button
            View buttonView = inflater.inflate(R.layout.color_button, null);
            Button button = buttonView.findViewById(R.id.button_content);
            ImageView iconCheck = buttonView.findViewById(R.id.icon_check);
            button.setText(colors[i]);

            button.setBackgroundColor(getResources().getColor(R.color.Bg_Light_2)); //màu mặc định
            button.setTextColor(getResources().getColor(R.color.black));
            iconCheck.setVisibility(View.GONE); // ẩn check

            View colorView = buttonView.findViewById(R.id.color);
            colorView.setBackgroundTintList(ColorStateList.valueOf(colorValues[i]));

            button.setOnClickListener(v -> {
                // update các button chưa chọn
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    View child = linearLayout.getChildAt(j);
                    Button btn = child.findViewById(R.id.button_content);
                    ImageView checkIcon = child.findViewById(R.id.icon_check);

                    btn.setBackgroundColor(getResources().getColor(R.color.Bg_Light_2));
                    btn.setTextColor(getResources().getColor(R.color.black));
                    checkIcon.setVisibility(View.GONE);
                }

                // update button được chọn
                button.setBackgroundColor(getResources().getColor(R.color.purple));
                button.setTextColor(getResources().getColor(R.color.white));
                iconCheck.setVisibility(View.VISIBLE);
            });

            // Thêm button vào LinearLayout
            linearLayout.addView(buttonView);
            buttons[i] = button;
        }

        //button đầu là dèault
        if (linearLayout.getChildCount() > 0) {
            View defaultButtonView = linearLayout.getChildAt(0);
            Button defaultButton = defaultButtonView.findViewById(R.id.button_content);
            ImageView defaultIconCheck = defaultButtonView.findViewById(R.id.icon_check);
            defaultButton.setBackgroundColor(getResources().getColor(R.color.purple));
            defaultButton.setTextColor(getResources().getColor(R.color.white));
            defaultIconCheck.setVisibility(View.VISIBLE);
        }
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

        LinearLayout linearLayout = dialogView.findViewById(R.id.overlay_sub_button);
        String[] sizes = {"S", "M", "L", "XL", "2XL"};

        Button[] buttons = new Button[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            View buttonView = inflater.inflate(R.layout.custom_button, null);
            Button button = buttonView.findViewById(R.id.button_content);
            ImageView iconCheck = buttonView.findViewById(R.id.icon_check);
            button.setText(sizes[i]);
            button.setBackgroundColor(getResources().getColor(R.color.Bg_Light_2));
            button.setTextColor(getResources().getColor(R.color.black));
            iconCheck.setVisibility(View.GONE);

            button.setOnClickListener(v -> {
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    View child = linearLayout.getChildAt(j);
                    Button btn = child.findViewById(R.id.button_content);
                    ImageView checkIcon = child.findViewById(R.id.icon_check);

                    btn.setBackgroundColor(getResources().getColor(R.color.Bg_Light_2));
                    btn.setTextColor(getResources().getColor(R.color.black));
                    checkIcon.setVisibility(View.GONE);
                }
                button.setBackgroundColor(getResources().getColor(R.color.purple));
                button.setTextColor(getResources().getColor(R.color.white));
                iconCheck.setVisibility(View.VISIBLE);
            });
            linearLayout.addView(buttonView);
            buttons[i] = button;
        }

        if (linearLayout.getChildCount() > 0) {
            View defaultButtonView = linearLayout.getChildAt(0);
            Button defaultButton = defaultButtonView.findViewById(R.id.button_content);
            ImageView defaultIconCheck = defaultButtonView.findViewById(R.id.icon_check);
            defaultButton.setBackgroundColor(getResources().getColor(R.color.purple));
            defaultButton.setTextColor(getResources().getColor(R.color.white));
            defaultIconCheck.setVisibility(View.VISIBLE);
        }
        bottomSheetDialog.show();
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