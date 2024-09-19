package com.example.ecommercemobileapp2hand.Views.Notifications;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.NotificationsHandler;
import com.example.ecommercemobileapp2hand.Models.Notifications;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.NotificationsAdapter;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    LinearLayout linearLayoutListNotifi, linearLayoutNoNotifi;
    RecyclerView recyclerViewNotifi;
    NotificationsAdapter adapter;
    List<Notifications> notificationsList;
    TextView textViewNoNotifi;
    Button buttonExplore;
    ImageView img_Bell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();

        Intent intent = getIntent();
        if (intent != null && "NotificationDetailFragment".equals(intent.getStringExtra("navigateTo"))) {
            NotificationDetailFragment fragment = new NotificationDetailFragment();


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recycler_view_notifications, fragment)
                    .commit();

            addControls();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Nhận tt user
        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("email");
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_email", email);
            editor.apply();
        }
    }

    void addControls()
    {
        linearLayoutListNotifi = (LinearLayout) findViewById(R.id.ListNotifi) ;
        linearLayoutNoNotifi = (LinearLayout) findViewById(R.id.No_NoTifi);
        textViewNoNotifi = (TextView) findViewById(R.id.tv_No_Notification);
        buttonExplore = (Button) findViewById(R.id.btn_Exploro_Categories);
        img_Bell = (ImageView) findViewById(R.id.img_Bell);
        recyclerViewNotifi = (RecyclerView) findViewById(R.id.recycler_view_notifications);
        recyclerViewNotifi.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationsAdapter(new ArrayList<>());
        recyclerViewNotifi.setAdapter(adapter);

    }
    void addEvents()
    {
        buttonExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(NotificationsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
//    void fetchNotifications()
//    {
//        new Thread(() ->{
//            notificationsList = NotificationsHandler.initNotificationList1();
//            runOnUiThread(() ->{
//                if (notificationsList == null || notificationsList.isEmpty())
//                {
//                    NoNotifi();
//                }else {
//
//                    if (adapter == null)
//                    {
//                        adapter = new NotificationsAdapter(notificationsList);
//                        recyclerViewNotifi.setAdapter(adapter);
//                    }else {
//                        adapter.setNotificationsList(notificationsList);
//                        adapter.notifyDataSetChanged();
//
//                    }
//                    HaveNotifi();
//                }
//
//            }
//
//            );
//        }
//
//        ).start();
//    }

    void NoNotifi()
    {
        linearLayoutNoNotifi.setVisibility(View.VISIBLE);
        linearLayoutListNotifi.setVisibility(View.GONE);
    }
    void HaveNotifi()
    {
        linearLayoutNoNotifi.setVisibility(View.GONE);
        linearLayoutListNotifi.setVisibility(View.VISIBLE);
    }


}