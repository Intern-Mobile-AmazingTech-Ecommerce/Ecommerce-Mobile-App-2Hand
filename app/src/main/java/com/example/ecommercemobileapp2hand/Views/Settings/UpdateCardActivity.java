package com.example.ecommercemobileapp2hand.Views.Settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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

import com.example.ecommercemobileapp2hand.Controllers.UserCardsHandler;
import com.example.ecommercemobileapp2hand.R;

import java.text.SimpleDateFormat;
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
    Dialog dialog;
    Button buttonDialogCancel, buttonDialogConfirm;
    TextView textViewTitle, textViewContent;
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

        dialog = new Dialog(UpdateCardActivity.this);
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

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            String expFormatted = outputFormat.format(inputFormat.parse(exp));

            editTextExp.setText(expFormatted);
        } catch (Exception e) {
            e.printStackTrace();
            editTextExp.setText(exp);
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
                updateCard();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTitle.setText("Warning !!!");
                textViewContent.setText("Do you want delete this card ?");
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
                deleteCard();
                dialog.dismiss();
                finish();
            }
        });
    }
    void deleteCard()
    {
        UserCardsHandler.deleteCardById(cardId, new UserCardsHandler.Callback<Boolean>(){
                    @Override
                    public void onResult(Boolean result) {
                        if (result)
                        {
                            Toast.makeText(getApplicationContext(), "Card đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Cập nhật Card thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    void updateCard() {
        if (validateInput()) {
            String cardNumber = String.valueOf(editTextCardNumber.getText());
            String ccv = String.valueOf(editTextCCV.getText());
            String exp = String.valueOf(editTextExp.getText());
            String cardHolder = String.valueOf(editTextCardHolder.getText());
            UserCardsHandler.updateByCardId(cardId, cardNumber, ccv, exp, cardHolder, new UserCardsHandler.Callback<Boolean>() {
                @Override
                public void onResult(Boolean result) {
                    runOnUiThread(() -> {
                        if (result) {
                            Toast.makeText(getApplicationContext(), "Update card success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Update card fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "Please check the information fields again", Toast.LENGTH_SHORT).show();
        }
    }
    boolean validateInput() {
        boolean isValid = true;

        // Card Holder Validation
        String cardHolderText = editTextCardHolder.getText().toString();
        if (cardHolderText.isEmpty() || !cardHolderText.matches("^[a-zA-Z ]+$")) {
            editTextCardHolder.setError("This field cannot be left blank and must contain only letters");
            isValid = false;
        }

        // Expiration Date Validation
        String expText = editTextExp.getText().toString();
        if (expText.isEmpty() || !expText.matches("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19|20)\\d{2}$")) {
            editTextExp.setError("Invalid format. Please enter date in dd-MM-YYYY.");
            isValid = false;
        }

        // Card Number Validation
        String cardNumberText = editTextCardNumber.getText().toString();
        if (cardNumberText.isEmpty()) {
            editTextCardNumber.setError("This field cannot be left blank");
            isValid = false;
        }

        // CCV Validation
        String ccvText = editTextCCV.getText().toString();
        if (ccvText.isEmpty() || ccvText.length() != 3) {
            editTextCCV.setError("This field cannot be left blank and only contains 3 numbers");
            isValid = false;
        }

        // Show toast for overall validation
        if (!isValid) {
            Toast.makeText(this, "Please check the information fields again", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }



}