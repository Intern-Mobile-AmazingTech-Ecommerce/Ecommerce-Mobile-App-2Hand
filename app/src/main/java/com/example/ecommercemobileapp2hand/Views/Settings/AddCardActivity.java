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

import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddCardActivity extends AppCompatActivity {

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
        addCard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvents();
        validateInput();
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
                DBConnect c = new DBConnect();
                connection = c.connectionClass();
                try{
                    if(connection != null){
                        String sqlinsert="Insert into user_cards values ('5','"+edtCardNumber.getText().toString()+"','"+edtCCV.getText().toString()+"','"+edtEXP.getText().toString()+"','"+edtCardholderName.getText().toString()+"')";
                        Statement st = connection.createStatement();
                        ResultSet rs = st.executeQuery(sqlinsert);
                    }
                }catch (Exception exception){
                    Log.e("Error", exception.getMessage());
                }
                Intent myintent = new Intent(AddCardActivity.this, PaymentActivity.class);
                startActivity(myintent);
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
                    Toast.makeText(AddCardActivity.this,"thành công",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(AddCardActivity.this,"thất bại",Toast.LENGTH_SHORT).show();
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
                if (edtCardNumber.getText().toString().isEmpty()){
                    edtCardNumber.setError("Trường này không được bỏ trống");
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
                    edtCCV.setError("Trường này không được bỏ trống");
                }
                if (edtCCV.getText().toString().length()!=3){
                    edtCCV.setError("Trường này chỉ chứa 3 số");
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
                if (edtEXP.getText().toString().isEmpty()){
                    edtEXP.setError("Trường này không được bỏ trống");
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
                    edtCardholderName.setError("Trường này không được bỏ trống");
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