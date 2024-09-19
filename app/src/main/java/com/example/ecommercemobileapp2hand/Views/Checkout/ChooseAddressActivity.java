package com.example.ecommercemobileapp2hand.Views.Checkout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.UserAddressHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.AddressAdapter;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.example.ecommercemobileapp2hand.Views.Orders.TrackOrderAcitivity;
import com.example.ecommercemobileapp2hand.Views.Settings.AddAddressActivity;
import com.example.ecommercemobileapp2hand.Views.Settings.ListAddressActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChooseAddressActivity extends AppCompatActivity {
    ExecutorService service = Executors.newSingleThreadExecutor();
    private ImageView imgBack;
    private RecyclerView recy_address;
    private CardView cv_address;
    private ArrayList<UserAddress> lstAddress;
    private AddressAdapter addressAdapter;
    private int addressID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        getAddressID();
    }
    @Override
    protected void onResume() {
        super.onResume();
        addEvents();
        loadListAddress();
    }

    private void addControls()
    {
        imgBack = findViewById(R.id.imgBack);
        recy_address = findViewById(R.id.recy_address);
        cv_address = findViewById(R.id.cv_address);
    }
    private void addEvents()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getAddressID(){
        Intent intent=getIntent();
        addressID=intent.getIntExtra("addressID",0);
    }
    private void loadListAddress() {
        service.execute(() -> {
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
//            String email = sharedPreferences.getString("userEmail", "");
//            UserAccount user = UserAccountHandler.getUserAccountByEmail(email);
            UserAccount user = UserAccountManager.getInstance().getCurrentUserAccount();
            if (user != null) {
                String userId = user.getUserId();
                UserAddressHandler.getListAdressByUserId(userId, new UserAddressHandler.Callback<ArrayList<UserAddress>>() {
                    @Override
                    public void onResult(ArrayList<UserAddress> result) {
                        lstAddress =result;
                        if (lstAddress != null && !lstAddress.isEmpty()) {
                            runOnUiThread(() -> {
                                addressAdapter = new AddressAdapter(lstAddress, ChooseAddressActivity.this, R.layout.custom_item_choose_address, new AddressAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("selected_item", position);
                                        setResult(RESULT_OK, resultIntent);
                                        finish();
                                    }
                                });
                                if (addressID>0){
                                    addressAdapter.setAddressID(addressID);
                                }
                                recy_address.setLayoutManager(new LinearLayoutManager(ChooseAddressActivity.this));
                                recy_address.setAdapter(addressAdapter);

                            });
                        }
                        else {
                            runOnUiThread(() -> {
                            });
                        }
                    }
                });
            }
        });
    }
}