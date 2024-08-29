package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
                onClickSignUp();
            }
        });
    }
    private void onClickSignUp(){
        String strEmail = ((EditText) findViewById(R.id.email_address)).getText().toString().trim();
        String strPass = ((EditText) findViewById(R.id.password)).getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(strEmail,strPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignUpActivity.this, OnboardingActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(SignUpActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                        }
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