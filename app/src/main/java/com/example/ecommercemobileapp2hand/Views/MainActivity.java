package com.example.ecommercemobileapp2hand.Views;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommercemobileapp2hand.Controllers.ProductObjectHandler;
import com.example.ecommercemobileapp2hand.Controllers.BagHandler;
import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.ProductObject;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;

import com.example.ecommercemobileapp2hand.Views.Adapters.GenderAdapter;
import com.example.ecommercemobileapp2hand.Views.Cart.Cart;
import com.example.ecommercemobileapp2hand.Views.Cart.EmptyCart;
import com.example.ecommercemobileapp2hand.Views.Homepage.HomeFragment;
import com.example.ecommercemobileapp2hand.Views.Notifications.NotificationDetailFragment;
import com.example.ecommercemobileapp2hand.Views.Notifications.NotificationsFragment;
import com.example.ecommercemobileapp2hand.Views.Orders.OrdersFragment;
import com.example.ecommercemobileapp2hand.Views.Settings.SettingsFragment;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static App app = new App();
    private String gender;
    private BottomNavigationView bottomNavigationView;
    private RelativeLayout actionBar;
    private FrameLayout frameLayout;
    private TextView tvFragmentName;
    private ImageButton btnAvt, btnBag;
    private MaterialButton btnObject;
    private ArrayList<String> listObj;
    private UserAccount userAccount;
    private SharedPreferences sharedPreferences;
    private String firstURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getIt();
        addControl();
        binding();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Nhận tt user từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("email");
            String userId = intent.getStringExtra("user_id");
            // Lưu vào sharedpreferences
            sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_email", email);
            editor.putString("user_id", userId);
            editor.apply();
        }
    }

    private void getIt() {
        userAccount = UserAccountManager.getInstance().getCurrentUserAccount();
        if (userAccount != null) {
            firstURL = userAccount.getImgUrl();
        } else {
            // Handle the case where userAccount is null
            firstURL = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent();
        addCart();
        loadAvt();
    }

    private void loadAvt() {
        UserAccount user = UserAccountManager.getInstance().getCurrentUserAccount();
        if (user != null) {
            String imgUrl = user.getImgUrl();
            if (imgUrl != null && !imgUrl.isEmpty()) {
                Util.getCloudinaryImageUrl(getApplicationContext(), imgUrl, -1, -1, new Util.Callback<String>() {
                    @Override
                    public void onResult(String result) {
                        String url = result;
                        if (!url.isEmpty()) {
                            runOnUiThread(() -> {
                                Glide.with(getApplicationContext()).load(url).into(btnAvt);
                            });
                        }
                    }
                });
            } else {
                Bitmap bitmap = Util.convertStringToBitmapFromAccess(this, "avt.png");
                btnAvt.setImageBitmap(bitmap);
            }
        } else {
            Bitmap bitmap = Util.convertStringToBitmapFromAccess(this, "avt.png");
            btnAvt.setImageBitmap(bitmap);
        }
    }

    private void addControl() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        actionBar = findViewById(R.id.action_bar);
        frameLayout = findViewById(R.id.frameLayout);
        tvFragmentName = findViewById(R.id.tvFragmentName);
        btnAvt = findViewById(R.id.btnAvt);
        btnBag = findViewById(R.id.btnBag);
        btnObject = findViewById(R.id.btnObject);
        sharedPreferences = getSharedPreferences("my_userID", MODE_PRIVATE);
        gender = sharedPreferences.getString("gender_key", "");
        if (gender.isEmpty()) {
            gender = "Men";
        }
        btnObject.setText(gender);
    }

    private void addCart() {
        btnBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Bag> listBag = BagHandler.getData(UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
                if (!listBag.isEmpty()) {
                    Intent myintent = new Intent(MainActivity.this, Cart.class);
                    startActivity(myintent);
                } else {
                    Intent myintent = new Intent(MainActivity.this, EmptyCart.class);
                    startActivity(myintent);
                }
            }
        });
    }

    private void binding() {
        Intent intent = getIntent();
        bottomNavigationView = findViewById(R.id.bottom_nav);
        if (intent != null && "SettingsFragment".equals(intent.getStringExtra("navigateTo")) && intent.getBooleanExtra("ActionBarOFF", false)) {
            btnObject.setVisibility(GONE);
            btnAvt.setVisibility(GONE);
            btnBag.setVisibility(GONE);
            tvFragmentName.setVisibility(View.GONE);

            LoadFragment(new SettingsFragment());
            bottomNavigationView.setSelectedItemId(R.id.itemSettings);
        } else if (intent != null && "OrdersFragment".equals(intent.getStringExtra("navigateTo"))) {
            LoadFragment(new OrdersFragment());
            bottomNavigationView.setSelectedItemId(R.id.itemOrders);
        } else if (intent != null && "NotificationsDetailFragment".equals(intent.getStringExtra("navigateTo"))) {
            LoadFragment(new NotificationsFragment());
            bottomNavigationView.setSelectedItemId(R.id.itemNotifications);
        } else {
            LoadFragment(new HomeFragment());
        }
    }

    private void addEvent() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.itemHome) {
                    if (firstURL != (userAccount.getImgUrl() != null ? userAccount.getImgUrl() : "") ) {
                        firstURL = userAccount.getImgUrl();
                        loadAvt();
                    }
                    LoadFragment(new HomeFragment());
                    btnObject.setVisibility(View.VISIBLE);
                    btnAvt.setVisibility(View.VISIBLE);
                    btnBag.setVisibility(View.VISIBLE);
                    tvFragmentName.setVisibility(View.GONE);

                    return true;
                } else if (id == R.id.itemNotifications) {
                    btnObject.setVisibility(GONE);
                    btnAvt.setVisibility(GONE);
                    btnBag.setVisibility(GONE);
                    LoadFragment(new NotificationDetailFragment());
                    tvFragmentName.setText("Notifications");
                    tvFragmentName.setVisibility(View.VISIBLE);
                    return true;
                } else if (id == R.id.itemOrders) {
                    btnObject.setVisibility(GONE);
                    btnAvt.setVisibility(GONE);
                    btnBag.setVisibility(GONE);
                    LoadFragment(new OrdersFragment());
                    tvFragmentName.setText("Orders");
                    tvFragmentName.setVisibility(View.VISIBLE);
                    return true;
                } else if (id == R.id.itemSettings) {
                    btnObject.setVisibility(GONE);
                    btnAvt.setVisibility(GONE);
                    btnBag.setVisibility(GONE);
                    tvFragmentName.setVisibility(View.GONE);
                    LoadFragment(new SettingsFragment());
                    return true;
                }
                return false;
            }
        });
        btnObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderOverlay("Gender");
            }
        });
    }

    private void LoadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserAccount", (Serializable) userAccount);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void showGenderOverlay(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.gender_overlay, null);

        bottomSheetDialog.setContentView(dialogView);
        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        TextView btn_clear = dialogView.findViewById(R.id.btn_clear_overlay);
        btn_clear.setVisibility(GONE);
        overlayTitle.setText(type);
        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        RecyclerView recy_gender = dialogView.findViewById(R.id.recy_gender);

        ProductObjectHandler.getData(new ProductObjectHandler.Callback<ArrayList<ProductObject>>() {
            @Override
            public void onResult(ArrayList<ProductObject> result) {
                ArrayList<ProductObject> genderArr = result;
                runOnUiThread(() -> {
                    GenderAdapter genderAdapter = new GenderAdapter(genderArr, MainActivity.this, btnObject.getText().toString(), new GenderAdapter.OnGenderSelectedListener() {
                        @Override
                        public void onGenderSelected(String selectedGender) {
                            btnObject.setText(selectedGender);
                            sharedPreferences = getSharedPreferences("my_userID", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("gender_key", selectedGender);
                            Boolean isSaved = editor.commit();
                            if (isSaved) {
                                LoadFragment(new HomeFragment());
                            }
                            bottomSheetDialog.dismiss();
                        }
                    });

                    recy_gender.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recy_gender.setItemAnimator(new DefaultItemAnimator());

                    recy_gender.setAdapter(genderAdapter);
                });
            }
        });

        bottomSheetDialog.show();
    }
}