package com.example.ecommercemobileapp2hand.Views.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Controllers.UserOrderHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Orders.OrderDetailsActivity;
import com.example.ecommercemobileapp2hand.Views.Orders.TrackOrderAcitivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderPlaceSuccessfullyActivity extends AppCompatActivity {
    private Button btnSeeOrderDetails;
    private UserAccount userAccount;
    private UserOrder ord;
    private ExecutorService service = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_place_successfully);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userAccount= UserAccountManager.getInstance().getCurrentUserAccount();
        service.execute(() -> {
            ord=UserOrderHandler.GetLatestUserOrder(userAccount.getUserId());
        });
        btnSeeOrderDetails=findViewById(R.id.btnSeeOrderDetails);
        btnSeeOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderPlaceSuccessfullyActivity.this, TrackOrderAcitivity.class);
                intent.putExtra("UserAccount", userAccount);
                intent.putExtra("order", ord);
                intent.putExtra("activity","orderPlace");
                startActivity(intent);
                finish();
            }
        });
    }
}