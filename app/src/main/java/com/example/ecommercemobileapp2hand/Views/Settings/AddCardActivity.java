package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;

import android.util.Log;

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

import com.example.ecommercemobileapp2hand.Controllers.UserCardsHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddCardActivity extends AppCompatActivity {
    private ExecutorService service;
    private ImageView imgBack;
    private EditText edtCardNumber, edtCCV, edtEXP, edtCardholderName;
    private Button btnSaveCard;
    Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_card);
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
        if(service==null ||service.isShutdown()){
            service = Executors.newSingleThreadExecutor();
        }
        addEvents();
        validateInput();
        addCard();
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
    private void addControls()
    {
        imgBack = findViewById(R.id.imgBack);
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtCCV = findViewById(R.id.edtCCV);
        edtEXP = findViewById(R.id.edtEXP);
        edtCardholderName = findViewById(R.id.edtCardholderName);
        btnSaveCard = findViewById(R.id.btnSaveCard);
    }

    private void addCard(){
        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNumber=edtCardNumber.getText().toString();
                String ccv=edtCCV.getText().toString();
                String exp=edtEXP.getText().toString();
                String cardHolderName=edtCardholderName.getText().toString();
                UserCardsHandler.insertCard(UserAccountManager.getInstance().getCurrentUserAccount().getUserId(), cardNumber, ccv, exp, cardHolderName, new UserCardsHandler.Callback<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        runOnUiThread(()->{
                            if(result){
                                Toast.makeText(getApplicationContext(),"Card Added Successfully",Toast.LENGTH_SHORT).show();

                                service.shutdown();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"Card Added Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }
        });
    }


    private void addEvents()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNumber=edtCardNumber.getText().toString();
                String ccv=edtCCV.getText().toString();
                String exp=edtEXP.getText().toString();
                String cardHolderName=edtCardholderName.getText().toString();
                if (!isEmpty(cardNumber,ccv,exp,cardHolderName)){
                    Toast.makeText(AddCardActivity.this,"Success",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(AddCardActivity.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validateInput(){
        edtCardholderName.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence != null && charSequence.toString().matches("^[a-zA-Z ]$")) {
                    return null;
                }
                return "";
            }
        }});
        edtEXP.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence != null && charSequence.toString().matches("^[0-9-]$")) {
                    return null;
                }
                return "";
            }
        }});
        edtCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cardNumber = edtCardNumber.getText().toString();
                if (cardNumber.isEmpty()) {
                    edtCardNumber.setError("This field cannot be left blank");
                    return;
                }
                if (cardNumber.length() != 16) {
                    edtCardNumber.setError("Card number must be exactly 16 digits");
                }
            }
        });
        edtCCV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtCCV.getText().toString().isEmpty()){
                    edtCCV.setError("This field cannot be left blank");
                }
                if (edtCCV.getText().toString().length()!=3){
                    edtCCV.setError("This field contains only 3 numbers");
                }
            }
        });
        edtEXP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String expDate = edtEXP.getText().toString();
                if (expDate.isEmpty()) {
                    edtEXP.setError("This field cannot be left blank");
                    return;
                }

                String regex = "^\\d{2}-\\d{2}-\\d{4}$";
                if (!expDate.matches(regex)) {
                    edtEXP.setError("Invalid format. Please enter date in dd-MM-YYYY.");
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dateFormat.setLenient(false);
                try {
                    dateFormat.parse(expDate);
                } catch (ParseException e) {
                    edtEXP.setError("Invalid date.");
                }
            }
        });
        edtCardholderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtCardholderName.getText().toString().isEmpty()){
                    edtCardholderName.setError("This field cannot be left blank");
                }
            }
        });
    }
    private boolean isEmpty(String cardNumber,String ccv,String exp,String cardHolderName){
        if (cardNumber.isEmpty()||ccv.isEmpty()||exp.isEmpty()||cardHolderName.isEmpty()){
            return true;
        }
        return false;
    }
}