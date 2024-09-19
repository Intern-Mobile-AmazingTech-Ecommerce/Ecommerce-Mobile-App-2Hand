package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserAddressHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.AddressAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListAddressActivity extends AppCompatActivity {
    ExecutorService service = Executors.newSingleThreadExecutor();
    private ImageView imgBack;
    private RecyclerView recy_address;
    private CardView cv_address;
    private ArrayList<UserAddress> lstAddress;
    private AddressAdapter addressAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();

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

        addressAdapter = new AddressAdapter(lstAddress, ListAddressActivity.this,R.layout.item_address);
        recy_address.setLayoutManager(new LinearLayoutManager(ListAddressActivity.this));
        recy_address.setAdapter(addressAdapter);
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
                Intent intent = new Intent(ListAddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });
    }
     private void loadListAddress() {
        service.execute(() -> {
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            UserAccount user = UserAccountManager.getInstance().getCurrentUserAccount();
            if (user != null) {
                String userId = user.getUserId();
                 UserAddressHandler.getListAdressByUserId(userId, new UserAddressHandler.Callback<ArrayList<UserAddress>>() {
                    @Override
                    public void onResult(ArrayList<UserAddress> result) {
                        lstAddress = result;
                        runOnUiThread(() -> {
                            addressAdapter.setUserAddressList(lstAddress);
                            if (lstAddress == null || lstAddress.isEmpty()) {
                                Toast.makeText(ListAddressActivity.this, "No address found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }
        });
}

}