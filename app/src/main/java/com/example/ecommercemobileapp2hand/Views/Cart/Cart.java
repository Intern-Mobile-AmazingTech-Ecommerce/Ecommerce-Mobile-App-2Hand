package com.example.ecommercemobileapp2hand.Views.Cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommercemobileapp2hand.Controllers.BagHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserAddressHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Views.Adapters.Adapter_Cart;
import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.AddressAdapter;
import com.example.ecommercemobileapp2hand.Views.Checkout.Checkout;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.example.ecommercemobileapp2hand.Views.Settings.ListAddressActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cart extends AppCompatActivity {
    ExecutorService service = Executors.newSingleThreadExecutor();
    ImageButton back;
    TextView removeAll,txtSubtotal,txtTax,txtTotal,txtShippingCost;
    Button btncheckout;
    ArrayList<Bag> mylist;
    Adapter_Cart myadapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtSubtotal=findViewById(R.id.txtSubtotal);
        txtTax=findViewById(R.id.txtTax);
        txtTotal=findViewById(R.id.txtTotal);
        txtShippingCost=findViewById(R.id.txtShippingCost);
        lv = findViewById(R.id.lv_cart);
        loadUserBag();
        //nut back
        back = findViewById(R.id.btnBack2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //nut checkout
        btncheckout = findViewById(R.id.btnCheckout);
        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(Cart.this, Checkout.class);
                myintent.putExtra("subtotal",txtSubtotal.getText().toString());
                myintent.putExtra("tax",txtTax.getText().toString());
                myintent.putExtra("shippingCost",txtShippingCost.getText().toString());
                myintent.putExtra("total",txtTotal.getText().toString());
                myintent.putExtra("listOrder",mylist);
                startActivity(myintent);
            }
        });
        //nut remove all
        removeAll=findViewById(R.id.removeAll);
        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BagHandler.deleteUserBag(UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
                loadUserBag();
            }
        });
    }

    private void loadUserBag()
    {
        service.execute(() -> {
//            SharedPreferences sharedPreferences = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
//            String email = sharedPreferences.getString("userEmail", "");
            UserAccount user = UserAccountManager.getInstance().getCurrentUserAccount();
            mylist = BagHandler.getData(user.getUserId());
            if (mylist != null && !mylist.isEmpty()) {
                runOnUiThread(() -> {
                    myadapter = new Adapter_Cart(Cart.this , R.layout.layout_cart,mylist);
                    lv.setAdapter(myadapter);
                });
            }
            else{
                Intent intent = new Intent(Cart.this,EmptyCart.class);
                startActivity(intent);
            }

        });
    }

}