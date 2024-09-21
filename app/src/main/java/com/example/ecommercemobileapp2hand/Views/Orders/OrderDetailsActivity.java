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

import com.example.ecommercemobileapp2hand.Controllers.UserOrderProductsHandler;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.Models.UserOrderProducts;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.OrderDetailsAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrderDetailsActivity extends AppCompatActivity {
    private ExecutorService service = Executors.newCachedThreadPool();
    private ImageView imgBack;
    private RecyclerView recy_orderdetails;
    private TextView tvMerchandise, tvShippingCost, tvDiscount, tvOrderTotal;
    private UserOrder order;
    private ArrayList<UserOrderProducts> lst;
    private OrderDetailsAdapter orderDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getIt();
        addControls();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserOrderProduct();
        addEvents();

    }
    public void onDestroy() {
        super.onDestroy();
        if (service != null && !service.isShutdown()) {
            service.shutdown();
            try {
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    private void loadUserOrderProduct() {
        UserOrderProductsHandler.getUserOrderProductsByOrderID(order.getUser_order_id(), new UserOrderProductsHandler.Callback<ArrayList<UserOrderProducts>>() {
            @Override
            public void onResult(ArrayList<UserOrderProducts> result) {
                lst = result;
                runOnUiThread(() -> {
                    if (!lst.isEmpty() && lst != null && order.getOrder_status_id() == 5) {
                        orderDetailsAdapter = new OrderDetailsAdapter(lst, OrderDetailsActivity.this, true);
                    }
                    else
                    {
                        orderDetailsAdapter = new OrderDetailsAdapter(lst, OrderDetailsActivity.this, false);
                    }
                    recy_orderdetails.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recy_orderdetails.setItemAnimator(new DefaultItemAnimator());
                    recy_orderdetails.setAdapter(orderDetailsAdapter);
                    tvMerchandise.setText("$" + String.valueOf(order.getTotal_price().add(order.getTotal_sale()).subtract(BigDecimal.valueOf(8).setScale(2, BigDecimal.ROUND_HALF_UP))));
                    tvShippingCost.setText("$" + String.valueOf(BigDecimal.valueOf(8).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    tvDiscount.setText("-$" + String.valueOf(order.getTotal_sale()));
                    tvOrderTotal.setText("$" + String.valueOf(order.getTotal_price()));
                });
            }
        });
    }

    private void addControls() {
        imgBack = findViewById(R.id.imgBack);
        recy_orderdetails = findViewById(R.id.recy_orderdetails);
        tvMerchandise = findViewById(R.id.tvMerchandiseSubtotal);
        tvShippingCost = findViewById(R.id.tvShippingCost);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void getIt() {
        Intent intent = getIntent();
        order = (UserOrder) intent.getSerializableExtra("Order");
    }
}