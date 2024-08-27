package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;

public class SignUpActivity extends AppCompatActivity {

    Button btnContinue;
    ImageButton btnReturn;
    TextView txtResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createAccount();
        returnToSignIn();
        resetPassword();
    }

    private boolean isValidFirstName(String firstName) {
        return !firstName.isEmpty();
    }

    private boolean isValidLastName(String lastName) {
        return !lastName.isEmpty();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private void returnToSignIn() {
        btnReturn = findViewById(R.id.btn_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });
    }

    private void createAccount() {
        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = ((EditText) findViewById(R.id.firstname)).getText().toString();
                String lastName = ((EditText) findViewById(R.id.lastname)).getText().toString();
                String email = ((EditText) findViewById(R.id.email_address)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                if (!isValidFirstName(firstName)) {
                    ((EditText) findViewById(R.id.firstname)).setError("Không được để trống");
                    return;
                }

                if (!isValidLastName(lastName)) {
                    ((EditText) findViewById(R.id.lastname)).setError("Không được để trống");
                    return;
                }

                if (!isValidEmail(email)) {
                    ((EditText) findViewById(R.id.email_address)).setError("Email không hợp lệ");
                    return;
                }

                if (!isValidPassword(password)) {
                    ((EditText) findViewById(R.id.password)).setError("Mật khẩu phải có ít nhất 6 ký tự");
                    return;
                }

                startActivity(new Intent(SignUpActivity.this, OnboardingActivity.class));
                finish();
            }
        });
    }

    private void resetPassword() {
        txtResetPassword = findViewById(R.id.dont_have_a);
        txtResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, ForgotPasswordActivity.class));
            }
        });
    }
}