package com.example.ecommercemobileapp2hand.Views.Cart;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Models.Adapter_Cart;
import com.example.ecommercemobileapp2hand.Models.Cartt;
import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    int image[] = {R.drawable.ao1, R.drawable.ao2};
    String name[] = {"ao vang","ao trang"};
    String size[] = {"Size M", "Size L"};
    String price[] = {"$150", "$250"};
    String color[] = {"Red","White"};
    //
    ArrayList<Cartt> mylist;
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
        lv = findViewById(R.id.lv_cart);
        mylist = new ArrayList<>();
        for(int i = 0; i< name.length; i++)
        {
            mylist.add(new Cartt(image[i], name[i],size[i], price[i], color[i]));
        }
        myadapter = new Adapter_Cart(Cart.this , R.layout.layout_cart,mylist);
        lv.setAdapter(myadapter);
    }
}