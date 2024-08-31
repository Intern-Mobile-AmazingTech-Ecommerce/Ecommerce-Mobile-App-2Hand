package com.example.ecommercemobileapp2hand.Views;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;

import com.example.ecommercemobileapp2hand.Views.Cart.Cart;
import com.example.ecommercemobileapp2hand.Views.Cart.EmptyCart;
import com.example.ecommercemobileapp2hand.Views.Homepage.HomeFragment;
import com.example.ecommercemobileapp2hand.Views.Notifications.NotificationDetailFragment;
import com.example.ecommercemobileapp2hand.Views.Notifications.NotificationsFragment;
import com.example.ecommercemobileapp2hand.Views.Orders.OrdersFragment;
import com.example.ecommercemobileapp2hand.Views.Settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RelativeLayout actionBar;
    private FrameLayout frameLayout;
    private TextView tvFragmentName;
    private ImageButton btnAvt,btnBag;
    private MaterialButton btnObject;
    private ArrayList<String> listObj;
    private UserAccount userAccount;


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
    protected void onResume() {
        super.onResume();
        addEvent();
        addCart();
    }

    private void addControl(){
        bottomNavigationView = findViewById(R.id.bottom_nav);
        actionBar = findViewById(R.id.action_bar);
        frameLayout = findViewById(R.id.frameLayout);
        tvFragmentName = findViewById(R.id.tvFragmentName);
        btnAvt = findViewById(R.id.btnAvt);
        btnBag = findViewById(R.id.btnBag);
        btnObject = findViewById(R.id.btnObject);

    }
    private void addCart(){
        btnBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(MainActivity.this, Cart.class);
                startActivity(myintent);
            }
        });
    }
    private void binding(){
        Intent intent = getIntent();
        bottomNavigationView = findViewById(R.id.bottom_nav);
        if (intent != null && "SettingsFragment".equals(intent.getStringExtra("navigateTo"))) {

            LoadFragment(new SettingsFragment());

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
            bottomNavigationView.setSelectedItemId(R.id.itemSettings);
        }else if(intent != null && "OrdersFragment".equals(intent.getStringExtra("navigateTo"))){
            LoadFragment(new OrdersFragment());

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
            bottomNavigationView.setSelectedItemId(R.id.itemOrders);
        }
        else if(intent != null && "NotificationsDetailFragment".equals(intent.getStringExtra("navigateTo"))){
            LoadFragment(new OrdersFragment());

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
            bottomNavigationView.setSelectedItemId(R.id.itemNotifications);
        }
        else{
            LoadFragment(new HomeFragment());
        }
    }
    private void addEvent(){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.itemHome) {

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
    }

    private void LoadFragment(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserAccount", userAccount);

        fragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void getIt()
    {
        Intent intent = getIntent();
        userAccount = (UserAccount) intent.getSerializableExtra("UserAccount");
    }
}