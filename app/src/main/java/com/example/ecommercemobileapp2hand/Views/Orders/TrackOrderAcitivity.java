package com.example.ecommercemobileapp2hand.Views.Orders;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
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
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.TrackOrderAdapter;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TrackOrderAcitivity extends AppCompatActivity {
    private ExecutorService service = Executors.newCachedThreadPool();
    ImageView img_Back;
    RecyclerView recy_status;
    TextView tv_orderid, tv_amount_order, tv_viewall, tv_shippingdetails;
    ArrayList<OrderStatus> orderStatuses;
    UserOrder order;
    UserAccount userAccount;
    TrackOrderAdapter trackOrderAdapter;
    String callingActivityName;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        getIt();
        loadOrderStatus();
        addevents();
    }
    @Override
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
    private void loadOrderStatus() {
        service.execute(() -> {
            orderStatuses = OrderStatusHandler.getData1();
            runOnUiThread(() -> {
                if (orderStatuses != null && !orderStatuses.isEmpty()) {
                    Collections.reverse(orderStatuses);

                    trackOrderAdapter = new TrackOrderAdapter(orderStatuses, TrackOrderAcitivity.this, order);

                    recy_status.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    recy_status.setItemAnimator(new DefaultItemAnimator());

                    recy_status.setAdapter(trackOrderAdapter);
                }

            });
        });


    }

    private void addcontrols() {
        img_Back = findViewById(R.id.imgBack);
        recy_status = findViewById(R.id.recy_status);
        tv_orderid = findViewById(R.id.tv_orderid);
        tv_amount_order = findViewById(R.id.tv_amount_order);
        tv_viewall = findViewById(R.id.tv_viewall);
        tv_shippingdetails = findViewById(R.id.tv_shippingdetails);

        tv_orderid.setText(tv_orderid.getText() + "#" + order.getUser_order_id());
        UserOrderProductsHandler.getAmountItems(order.getUser_order_id(), new UserOrderProductsHandler.Callback<Integer>() {
            @Override
            public void onResult(Integer result) {
                runOnUiThread(()->{
                    tv_amount_order.setText(String.valueOf(result + " items"));
                });

            }
        });

        UserAddressHandler.getListAdressByUserId(userAccount.getUserId(), new UserAddressHandler.Callback<ArrayList<UserAddress>>() {
            @Override
            public void onResult(ArrayList<UserAddress> result) {
                String s1 = "";
                for (UserAddress address : result)
                {
                    if (address.getUser_address_id() == order.getUser_address_id())
                    {
                        s1 += address.getUser_address_street() + " " + address.getUser_address_city() + ", " + address.getUser_address_state() + " " + address.getUser_address_zipcode() + "\n" + address.getUser_address_phone();
                    }
                }
                String finalS = s1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_shippingdetails.setText(finalS);
                    }
                });

            }
        });

    }

    void addevents() {
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingActivityName!=null){
                    if (callingActivityName.equals("com.example.ecommercemobileapp2hand.Views.Checkout.OrderPlaceSuccessfullyActivity")){
                        Intent intent = new Intent(TrackOrderAcitivity.this, MainActivity.class);
                        intent.putExtra("email",userAccount.getEmail());
                        intent.putExtra("user_id",userAccount.getUserId());
                        startActivity(intent);
                    }
                }
                else{
                    finish();
                }
            }
        });
        tv_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackOrderAcitivity.this, OrderDetailsActivity.class);
                intent.putExtra("Order", order);
                startActivity(intent);
            }
        });
    }

    void getIt() {
        Intent intent = getIntent();
        userAccount = (UserAccount) intent.getSerializableExtra("UserAccount");
        order = (UserOrder) intent.getSerializableExtra("order");
        ComponentName callingActivity=getCallingActivity();
        if (callingActivity!=null){
            callingActivityName= callingActivity.getClassName();
        }
    }
}