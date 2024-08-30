package com.example.ecommercemobileapp2hand.Views.Orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.OrderStatusHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserAddressHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserOrderProductsHandler;
import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.TrackOrderAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class TrackOrderAcitivity extends AppCompatActivity {

    ImageView img_Back;
    RecyclerView recy_status;
    TextView tv_orderid, tv_amount_order, tv_viewall, tv_shippingdetails;
    ArrayList<OrderStatus> orderStatuses;
    UserOrder order;
    TrackOrderAdapter trackOrderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_acitivity);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getIt();
        addcontrols();
        addevents();
    }
    void addcontrols()
    {
        img_Back = findViewById(R.id.imgBack);
        recy_status = findViewById(R.id.recy_status);
        tv_orderid = findViewById(R.id.tv_orderid);
        tv_amount_order = findViewById(R.id.tv_amount_order);
        tv_viewall = findViewById(R.id.tv_viewall);
        tv_shippingdetails = findViewById(R.id.tv_shippingdetails);

        tv_orderid.setText(tv_orderid.getText() + "#" + order.getUser_order_id());
        tv_amount_order.setText(String.valueOf(UserOrderProductsHandler.getAmountItems(order.getUser_order_id()) + " items"));

        tv_shippingdetails.setText(UserAddressHandler.getAddress(order.getUser_order_id()));

        orderStatuses = OrderStatusHandler.getData1();
        Collections.reverse(orderStatuses);

        trackOrderAdapter = new TrackOrderAdapter(orderStatuses, TrackOrderAcitivity.this, order);

        recy_status.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recy_status.setItemAnimator(new DefaultItemAnimator());

        recy_status.setAdapter(trackOrderAdapter);
    }
    void addevents()
    {
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    void getIt()
    {
        Intent intent = getIntent();
        order = (UserOrder) intent.getSerializableExtra("order");
    }
}