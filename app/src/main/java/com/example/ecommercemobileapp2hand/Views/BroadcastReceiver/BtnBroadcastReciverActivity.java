package com.example.ecommercemobileapp2hand.Views.BroadcastReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Models.FakeModels.Order;
import com.example.ecommercemobileapp2hand.Models.Notifications;
import com.example.ecommercemobileapp2hand.R;

public class BtnBroadcastReciverActivity extends AppCompatActivity {

    OrderChangeReceiver orderChangeReceiver;
    private static final String ORDER_STATUS_CHANGED= "ORDER_STATUS_CHANGED";
    Button btn1;
    private Order userOrder;
    private Notifications userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_btn_broadcast_reciver);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        orderChangeReceiver = new OrderChangeReceiver();
    }
    private void addcontrols() {
        btn1 =findViewById(R.id.btndemobroadcast);
    }

    @Override
    protected void onStart() {

        super.onStart();
        IntentFilter filter = new IntentFilter("ORDER_STATUS_CHANGED");
        registerReceiver(orderChangeReceiver, filter);
        addcontrols();
    }

    @Override
    protected void onStop() {

        super.onStop();
        unregisterReceiver(orderChangeReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        addEvents();
    }
    private void addEvents(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClickSendBroadcastReciver();
            }
        });
    }
    private void ClickSendBroadcastReciver(){
        Intent intent = new Intent(this, OrderChangeReceiver.class);
        intent.setAction(ORDER_STATUS_CHANGED);
        intent.putExtra("order_id", userOrder.getUser_order_id());
        intent.putExtra("order_status", "Đã giao hàng"); // Trạng thái ví dụ (Cần phải chuyển mã thành tên trạng thái)
        intent.putExtra("user_id", userid.getUser_id()); //
        sendBroadcast(intent);
    }



}