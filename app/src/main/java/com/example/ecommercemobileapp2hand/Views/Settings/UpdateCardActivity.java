package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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

import com.example.ecommercemobileapp2hand.Controllers.UserCardsHandler;
import com.example.ecommercemobileapp2hand.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateCardActivity extends AppCompatActivity {

    ExecutorService service = Executors.newSingleThreadExecutor();
    EditText editTextCardNumber, editTextCCV, editTextExp, editTextCardHolder;
    Button buttonSave, buttonDelete;
    ImageView imgBack;
    int cardId;
    AlertDialog.Builder builder;
    boolean isValid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_card);
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
        editTextCCV = findViewById(R.id.edtCCV);
        editTextCardNumber = findViewById(R.id.edtCardNumber);
        editTextExp = findViewById(R.id.edtEXP);
        editTextCardHolder = findViewById(R.id.edtCardholderName);
        buttonSave = findViewById(R.id.btnSaveCard);
        imgBack = findViewById(R.id.imgBack);
        buttonDelete = findViewById(R.id.btnDeleteCard);
        builder = new AlertDialog.Builder(this);
        loadData();
    }
    void loadData()
    {
        Intent intent = getIntent();
        cardId = intent.getIntExtra("id",0);
        String ccv = intent.getStringExtra("ccv");
        String cardNumber = intent.getStringExtra("cardNumber");
        String exp = intent.getStringExtra("exp");
        String cardHolder = intent.getStringExtra("hold");
        editTextCCV.setText(ccv);
        editTextCardNumber.setText(cardNumber);
        editTextCardHolder.setText(cardHolder);
        editTextExp.setText(exp);
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
                updateCard();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Warning!!!")
                        .setMessage("Bạn có muốn xoá card này ?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteCard();
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
    void deleteCard()
    {
        service.submit(()->{
            boolean isDelete = UserCardsHandler.deleteCardById(cardId);
            this.runOnUiThread(()->{
                if (isDelete)
                {
                    Toast.makeText(getApplicationContext(), "Card đã được xoá thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Xoá card thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    void updateCard() {
        if (validateInput()) {
            service.submit(() -> {
                String cardNumber = String.valueOf(editTextCardNumber.getText());
                String ccv = String.valueOf(editTextCCV.getText());
                String exp = String.valueOf(editTextExp.getText());
                String cardHolder = String.valueOf(editTextCardHolder.getText());
                boolean isUpdated = UserCardsHandler.updateByCardId(cardId, cardNumber, ccv, exp, cardHolder);
                this.runOnUiThread(() -> {
                    if (isUpdated) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Card đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cập nhật card thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            Toast.makeText(this, "Vui lòng kiểm tra lại các trường thông tin", Toast.LENGTH_SHORT).show();
        }
    }
    boolean validateInput() {
        boolean isValid = true;

        // Card Holder Validation
        String cardHolderText = editTextCardHolder.getText().toString();
        if (cardHolderText.isEmpty() || !cardHolderText.matches("^[a-zA-Z ]+$")) {
            editTextCardHolder.setError("Trường này không được bỏ trống và chỉ chứa chữ cái");
            isValid = false;
        }

        // Expiration Date Validation
        String expText = editTextExp.getText().toString();
        if (expText.isEmpty() || !expText.matches("^[0-9/-]+$")) {
            editTextExp.setError("Trường này không được bỏ trống và phải đúng định dạng");
            isValid = false;
        }

        // Card Number Validation
        String cardNumberText = editTextCardNumber.getText().toString();
        if (cardNumberText.isEmpty()) {
            editTextCardNumber.setError("Trường này không được bỏ trống");
            isValid = false;
        }

        // CCV Validation
        String ccvText = editTextCCV.getText().toString();
        if (ccvText.isEmpty() || ccvText.length() != 3) {
            editTextCCV.setError("Trường này không được bỏ trống và chỉ chứa 3 số");
            isValid = false;
        }

        // Show toast for overall validation
        if (!isValid) {
            Toast.makeText(this, "Vui lòng kiểm tra lại các trường thông tin", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }



}