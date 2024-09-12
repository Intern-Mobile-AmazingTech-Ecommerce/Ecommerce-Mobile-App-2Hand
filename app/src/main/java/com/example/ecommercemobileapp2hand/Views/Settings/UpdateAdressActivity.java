package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Controllers.UserAddressHandler;
import com.example.ecommercemobileapp2hand.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateAdressActivity extends AppCompatActivity {

    ExecutorService service = Executors.newSingleThreadExecutor();
    EditText editTextStreet, editTextCity, editTextState, editTextZipCode, editTextPhone;
    Button buttonSave;
    ImageView imgBack;
    int addressId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_adress);
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
    }

    void addControls()
    {
        editTextStreet = findViewById(R.id.edtStreetAddress);
        editTextCity = findViewById(R.id.edtCity);
        editTextPhone = findViewById(R.id.edtPhone);
        editTextState = findViewById(R.id.edtState);
        editTextZipCode = findViewById(R.id.edtZipCode);
        buttonSave = findViewById(R.id.btnSaveAddress);
        imgBack = findViewById(R.id.imgBack);
        loadData();
    }
    void loadData() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        addressId = id;
        String street = intent.getStringExtra("street");
        String city = intent.getStringExtra("city");
        String phone = intent.getStringExtra("phone");
        String state = intent.getStringExtra("state");
        String zip = intent.getStringExtra("zip");
        System.out.println("IDDDD"+id);
        if (street != null) {
            editTextStreet.setText(street);
        }
        if (city != null) {
            editTextCity.setText(city);
        }
        if (phone != null) {
            editTextPhone.setText(phone);
        }
        if (state != null) {
            editTextState.setText(state);
        }
        if (zip != null) {
            editTextZipCode.setText(zip);
        }
    }

    void addEvents()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAddress();
            }
        });
    }
    void updateAddress()
    {
        service.submit(()->{
            String street = String.valueOf(editTextStreet.getText());
            String city = String.valueOf(editTextCity.getText());
            String phone = String.valueOf(editTextPhone.getText());
            String state = String.valueOf(editTextState.getText());
            String zip = String.valueOf(editTextZipCode.getText());
            boolean isUpdated = UserAddressHandler.updateAddressById(addressId,street,city,state,zip,phone);
            this.runOnUiThread(()->{
                if (isUpdated) {
                    finish();
                    Toast.makeText(getApplicationContext(), "Địa chỉ đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cập nhật địa chỉ thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            });
        });



    }
}