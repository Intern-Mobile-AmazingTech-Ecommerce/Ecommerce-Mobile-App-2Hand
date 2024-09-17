package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Controllers.UserAddressHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddAddressActivity extends AppCompatActivity {
    private ExecutorService service;
    private ImageView imgBack;
    private EditText edtStreetAddress, edtCity, edtState, edtZipCode, edtPhoneNumber;
    private Button btnSaveAddress;
    Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_address);
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
        if (service == null || service.isShutdown()) {
            service = Executors.newSingleThreadExecutor();
        }
        addEvents();
        validateInput();
        addAddress();
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

    private void addControls() {
        imgBack = findViewById(R.id.imgBack);
        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        edtCity = findViewById(R.id.edtCity);
        edtState = findViewById(R.id.edtState);
        edtZipCode = findViewById(R.id.edtZipCode);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        edtPhoneNumber = findViewById(R.id.edtPhone);
    }

    private void addAddress() {
        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String streetAddress = edtStreetAddress.getText().toString();
                String city = edtCity.getText().toString();
                String state = edtState.getText().toString();
                String zipCode = edtZipCode.getText().toString();
                String phone = edtPhoneNumber.getText().toString();
                UserAddressHandler.insertAddress(UserAccountManager.getInstance().getCurrentUserAccount().getUserId(), streetAddress, city, state, zipCode, phone, new UserAddressHandler.Callback<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        if (result) {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Address Added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Address Added Failed", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });

            }
        });
    }


    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String streetAddress = edtStreetAddress.getText().toString();
                String city = edtCity.getText().toString();
                String state = edtState.getText().toString();
                String zipCode = edtZipCode.getText().toString();
                if (!isEmpty(streetAddress, city, state, zipCode)) {
                    Toast.makeText(AddAddressActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(AddAddressActivity.this, "thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateInput() {
        String allowRegex = "^[a-zA-Z0-9 /]$";
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && source.toString().matches(allowRegex)) {
                    return null;
                }
                return "";
            }
        };
        edtStreetAddress.setFilters(new InputFilter[]{filter});
        edtCity.setFilters(new InputFilter[]{filter});
        edtState.setFilters(new InputFilter[]{filter});
        edtStreetAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtStreetAddress.getText().toString().isEmpty()) {
                    edtStreetAddress.setError("Street address is required");
                }
            }
        });
        edtState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtState.getText().toString().isEmpty()) {
                    edtState.setError("State is required");
                }
            }
        });
        edtCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtCity.getText().toString().isEmpty()) {
                    edtCity.setError("City is required");
                }
            }
        });
        edtZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtZipCode.getText().toString().isEmpty()) {
                    edtZipCode.setError("Zip code is required");
                }
            }
        });
    }

    private boolean isEmpty(String streetAddress, String city, String state, String zipCode) {
        if (streetAddress.isEmpty() || city.isEmpty() || state.isEmpty() || zipCode.isEmpty()) {
            return true;
        }
        return false;
    }
}