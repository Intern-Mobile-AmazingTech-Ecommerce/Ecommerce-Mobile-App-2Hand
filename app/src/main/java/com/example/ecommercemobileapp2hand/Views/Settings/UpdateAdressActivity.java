package com.example.ecommercemobileapp2hand.Views.Settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
    Dialog dialog;
    Button buttonDialogCancel, buttonDialogConfirm;
    TextView textViewTitle, textViewContent;
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
        dialog = new Dialog(UpdateAdressActivity.this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.shapedialog));
        dialog.setCancelable(false);
        buttonDialogConfirm = dialog.findViewById(R.id.btnDialogConfirm);
        buttonDialogCancel = dialog.findViewById(R.id.btnDialogCancel);
        textViewContent = dialog.findViewById(R.id.txtContent);
        textViewTitle = dialog.findViewById(R.id.txtTitle);
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
            public void onClick(View view) {
                textViewTitle.setText("Warning !!!");
                textViewContent.setText("Do you want delete this address ?");
                dialog.show();
            }
        });
        buttonDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buttonDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress();
                dialog.dismiss();
                finish();
            }
        });
    }
    void deleteAddress()
    {
        UserAddressHandler.deleteAddressById(addressId, new UserAddressHandler.Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                runOnUiThread(()->{
                    if (result)
                    {
                        Toast.makeText(getApplicationContext(), "Delete address success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Delete address fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    void updateAddress() {
        if (isValid()) {
            String street = String.valueOf(editTextStreet.getText());
            String city = String.valueOf(editTextCity.getText());
            String phone = String.valueOf(editTextPhone.getText());
            String state = String.valueOf(editTextState.getText());
            String zip = String.valueOf(editTextZipCode.getText());
            UserAddressHandler.updateAddressById(addressId, street, city, state, zip, phone, new UserAddressHandler.Callback<Boolean>() {
                @Override
                public void onResult(Boolean result) {
                    runOnUiThread(() -> {
                        if (result) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Update address success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Update address fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
    boolean isValid() {
        boolean isValid = true;

        // Street Validation
        String street = editTextStreet.getText().toString();
        if (street.isEmpty()) {
            editTextStreet.setError("Street is required");
            isValid = false;
        }

        // City Validation
        String city = editTextCity.getText().toString();
        if (city.isEmpty()) {
            editTextCity.setError("Cityis required");
            isValid = false;
        }

        // Phone Validation
        String phone = editTextPhone.getText().toString();
        if (phone.isEmpty() || !phone.matches("\\d{10,11}")) { // Assuming a phone number between 10 and 15 digits
            editTextPhone.setError("Phone number is required and must contain between 10 and 11 digits.");
            isValid = false;
        }

        // State Validation
        String state = editTextState.getText().toString();
        if (state.isEmpty()) {
            editTextState.setError("State address is required");
            isValid = false;
        }

        // Zip Code Validation
        String zip = editTextZipCode.getText().toString();
        if (zip.isEmpty() || !zip.matches("\\d{5}(-\\d{4})?")) {
            editTextZipCode.setError("Zip code is required and must contain 5 digits.");
            isValid = false;
        }
        if (!isValid) {
            Toast.makeText(this, "Please check the information fields again.", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }
}