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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.Order;
import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Orders.CustomAdapter.TrackOrderAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class TrackOrderAcitivity extends AppCompatActivity {

    ImageView img_Back;
    RecyclerView recy_status;
    TextView tv_orderid, tv_amount_order, tv_viewall, tv_shippingdetails;
    ArrayList<OrderStatus> orderStatuses;
    Order order;
    TrackOrderAdapter trackOrderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_acitivity);
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
        tv_amount_order.setText(String.valueOf(order.getAmount()) + " items");

        tv_shippingdetails.setText(order.getShipping_details());

        orderStatuses = OrderStatus.initOrderStatus();

        ArrayList<OrderStatus> filter = filterStatus();
        trackOrderAdapter = new TrackOrderAdapter(filter, TrackOrderAcitivity.this, order);

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
        order = (Order) intent.getSerializableExtra("order");
    }
    private ArrayList<OrderStatus> filterStatus()
    {
        ArrayList<OrderStatus> filter = new ArrayList<>();
        for (OrderStatus orderStatus : orderStatuses)
        {
            if (order.getOrder_status_id() == orderStatus.getOrder_status_id())
            {
                orderStatus.setCompleted(true);
                filter.add(orderStatus);
                break;
            }
            else
            {
                orderStatus.setCompleted(true);
                filter.add(orderStatus);
            }
        }

        if (filter.size() < 4)
        {
            for (OrderStatus orderStatus : orderStatuses)
            {
                if (!filter.contains(orderStatus))
                {
                    filter.add(orderStatus);
                    if (orderStatus.getOrder_status_name().equals("Delivered"))
                    {
                        break;
                    }
                }
            }
        }

        Collections.reverse(filter);
        return filter;
    }
}