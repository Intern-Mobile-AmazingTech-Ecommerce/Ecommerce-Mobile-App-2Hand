package com.example.ecommercemobileapp2hand.Views;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Homepage.HomeFragment;
import com.example.ecommercemobileapp2hand.Views.Notifications.NotificationsFragment;
import com.example.ecommercemobileapp2hand.Views.Orders.OrdersFragment;
import com.example.ecommercemobileapp2hand.Views.Settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RelativeLayout actionBar;
    private FrameLayout frameLayout;
    private TextView tvFragmentName;
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
        addControl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent();
    }

    private void addControl(){
        bottomNavigationView = findViewById(R.id.bottom_nav);
        actionBar = findViewById(R.id.action_bar);
        frameLayout = findViewById(R.id.frameLayout);
        tvFragmentName = findViewById(R.id.tvFragmentName);
    }
    private void addEvent(){
        LoadFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.itemHome) {

                    LoadFragment(new HomeFragment());
                    tvFragmentName.setVisibility(View.GONE);
                    return true;
                } else if (id == R.id.itemNotifications) {

                    LoadFragment(new NotificationsFragment());
                    tvFragmentName.setText("Notifications");
                    tvFragmentName.setVisibility(View.VISIBLE);
                    return true;
                } else if (id == R.id.itemOrders) {

                    LoadFragment(new OrdersFragment());
                    tvFragmentName.setText("Orders");
                    tvFragmentName.setVisibility(View.VISIBLE);
                    return true;
                } else if (id == R.id.itemSettings) {

                    tvFragmentName.setVisibility(View.GONE);
                    LoadFragment(new SettingsFragment());
                    return true;
                }
                return false;
            }
        });
    }
    private void LoadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}