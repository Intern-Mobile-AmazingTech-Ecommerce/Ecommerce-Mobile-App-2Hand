package com.example.ecommercemobileapp2hand.Views;

import static android.view.View.GONE;

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

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.CustomAdapter.CustomObjectSpinnerAdapter;
import com.example.ecommercemobileapp2hand.Views.Homepage.HomeFragment;
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
    private Spinner spinnerObj;
    private ArrayList<String> listObj;
    private CustomObjectSpinnerAdapter spinnerObjectAdapter;
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
        LoadObjectSpinner();
    }

    private void addControl(){
        bottomNavigationView = findViewById(R.id.bottom_nav);
        actionBar = findViewById(R.id.action_bar);
        frameLayout = findViewById(R.id.frameLayout);
        tvFragmentName = findViewById(R.id.tvFragmentName);
        btnAvt = findViewById(R.id.btnAvt);
        btnBag = findViewById(R.id.btnBag);
        spinnerObj = findViewById(R.id.spinnerObject);

    }
    private void addEvent(){
        LoadFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.itemHome) {

                    LoadFragment(new HomeFragment());
                    spinnerObj.setVisibility(View.VISIBLE);
                    btnAvt.setVisibility(View.VISIBLE);
                    btnBag.setVisibility(View.VISIBLE);
                    tvFragmentName.setVisibility(View.GONE);
                    return true;
                } else if (id == R.id.itemNotifications) {

                    spinnerObj.setVisibility(GONE);
                    btnAvt.setVisibility(GONE);
                    btnBag.setVisibility(GONE);
                    LoadFragment(new NotificationsFragment());
                    tvFragmentName.setText("Notifications");
                    tvFragmentName.setVisibility(View.VISIBLE);
                    return true;
                } else if (id == R.id.itemOrders) {
                    spinnerObj.setVisibility(GONE);
                    btnAvt.setVisibility(GONE);
                    btnBag.setVisibility(GONE);
                    LoadFragment(new OrdersFragment());
                    tvFragmentName.setText("Orders");
                    tvFragmentName.setVisibility(View.VISIBLE);
                    return true;
                } else if (id == R.id.itemSettings) {
                    spinnerObj.setVisibility(GONE);
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
    private void LoadObjectSpinner(){
        listObj = new ArrayList<String>();
        listObj.add("Men");
        listObj.add("Women");
        spinnerObjectAdapter = new CustomObjectSpinnerAdapter(listObj);
        spinnerObj.setAdapter(spinnerObjectAdapter);
    }
    private void LoadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}