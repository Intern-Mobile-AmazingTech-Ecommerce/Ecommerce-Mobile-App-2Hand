package com.example.ecommercemobileapp2hand.Views.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Models.Adapter_Cart;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Checkout.Checkout;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    ImageButton btnBack;
    Button btnCheckout;
    //
    int image[] = {R.drawable.ao2, R.drawable.ao1};
    String name[] = {"Men's Harrington Jacket", "Men's Coaches Jacket"};
    String size[] = {"Size - M","Size - M"};
    String coler[] = {"Color - Lemon","Color - Black"};
    String price[] = {"$148","$52.00"};
    //
    ArrayList<com.example.ecommercemobileapp2hand.Models.Cart> mylist;
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
        //Nut back
        btnBack = findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(Cart.this, MainActivity.class);
                startActivity(myintent);
            }
        });
        //Nut Checkout
        btnCheckout = findViewById(R.id.btn_Checkout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(Cart.this, Checkout.class);
                startActivity(myintent);
            }
        });

        //
        lv = findViewById(R.id.lv_cart);
        mylist = new ArrayList<>();
        for(int i = 0; i< name.length; i++)
        {
            mylist.add(new com.example.ecommercemobileapp2hand.Models.Cart(image[i], name[i], size[i], coler[i], price[i]));
        }
        myadapter = new Adapter_Cart(Cart.this , R.layout.layout_cart,mylist);
        lv.setAdapter(myadapter);
    }
}