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

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    private ImageView imgBack;
    private RecyclerView recy_orderdetails;
    private TextView tvtotal_price;
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
        addEvents();
    }
    private void addControls()
    {
        imgBack = findViewById(R.id.imgBack);
        recy_orderdetails = findViewById(R.id.recy_orderdetails);
        tvtotal_price = findViewById(R.id.tvtotal_price);

        lst = UserOrderProductsHandler.getUserOrderProductsByOrderID(order.getUser_order_id());

        orderDetailsAdapter = new OrderDetailsAdapter(lst, OrderDetailsActivity.this);
        recy_orderdetails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recy_orderdetails.setItemAnimator(new DefaultItemAnimator());

        recy_orderdetails.setAdapter(orderDetailsAdapter);

        tvtotal_price.setText("Total price: $" + String.valueOf(order.getTotal_price()));
    }
    private void addEvents()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    void getIt()
    {
        Intent intent = getIntent();
        order = (UserOrder) intent.getSerializableExtra("Order");
    }
}