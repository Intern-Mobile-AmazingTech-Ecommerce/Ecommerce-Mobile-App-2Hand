package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
    Button buttonSave, buttonDelete;
    ImageView imgBack;
    int addressId;
    AlertDialog.Builder builder;
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
        buttonDelete = findViewById(R.id.btnDeleteAddress);
        imgBack = findViewById(R.id.imgBack);
        builder = new AlertDialog.Builder(this);
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
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               builder.setTitle("Warning!!!")
                       .setMessage("Bạn có muốn xoá địa chỉ này ?")
                       .setCancelable(true)
                       .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               deleteAddress();
                           }
                       })
                       .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.cancel();
                           }
                       }).show();
            }
        });
    }
    void deleteAddress()
    {
        service.submit(()->{
            boolean isDelete = UserAddressHandler.deleteAddressById(addressId);
            this.runOnUiThread(()->{
                if (isDelete)
                {
                    Toast.makeText(getApplicationContext(), "Địa chỉ đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Cập nhật địa chỉ thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    void updateAddress() {
        if (isValid()) {
            service.submit(() -> {
                String street = String.valueOf(editTextStreet.getText());
                String city = String.valueOf(editTextCity.getText());
                String phone = String.valueOf(editTextPhone.getText());
                String state = String.valueOf(editTextState.getText());
                String zip = String.valueOf(editTextZipCode.getText());
                boolean isUpdated = UserAddressHandler.updateAddressById(addressId, street, city, state, zip, phone);
                this.runOnUiThread(() -> {
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
    boolean isValid() {
        boolean isValid = true;

        // Street Validation
        String street = editTextStreet.getText().toString();
        if (street.isEmpty()) {
            editTextStreet.setError("Trường này không được bỏ trống");
            isValid = false;
        }

        // City Validation
        String city = editTextCity.getText().toString();
        if (city.isEmpty()) {
            editTextCity.setError("Trường này không được bỏ trống");
            isValid = false;
        }

        // Phone Validation
        String phone = editTextPhone.getText().toString();
        if (phone.isEmpty() || !phone.matches("\\d{10,11}")) { // Assuming a phone number between 10 and 15 digits
            editTextPhone.setError("Trường này không được bỏ trống và phải chứa từ 10 đến 11 số");
            isValid = false;
        }

        // State Validation
        String state = editTextState.getText().toString();
        if (state.isEmpty()) {
            editTextState.setError("Trường này không được bỏ trống");
            isValid = false;
        }

        // Zip Code Validation
        String zip = editTextZipCode.getText().toString();
        if (zip.isEmpty() || !zip.matches("\\d{5}(-\\d{4})?")) {
            editTextZipCode.setError("Trường này không được bỏ trống và phải đúng định dạng ZIP");
            isValid = false;
        }

        // Show overall error message if not valid
        if (!isValid) {
            Toast.makeText(this, "Vui lòng kiểm tra lại các trường thông tin", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }
}