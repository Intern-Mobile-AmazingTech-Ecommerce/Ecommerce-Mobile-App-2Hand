package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnContinue_2;
    ImageButton btnReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sendEmailReset();
        returnToSignInPassword();
        initialUI();
    }

    private void initialUI() {
        Button btnContinue = findViewById(R.id.btnContinue_2);
        EditText edtEmail = findViewById(R.id.email_address);
        btnContinue.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            if (!isValidEmail(email)) {
                ((EditText) findViewById(R.id.email_address)).setError("Email không hợp lệ");
                return;
            }
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendEmailReset(){
        btnContinue_2=(Button) findViewById(R.id.btnContinue_2);
        btnContinue_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this,returnlogin.class));
                finish();
            }
        });
    }
    private void returnToSignInPassword(){
        btnReturn=(ImageButton) findViewById(R.id.btn_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}