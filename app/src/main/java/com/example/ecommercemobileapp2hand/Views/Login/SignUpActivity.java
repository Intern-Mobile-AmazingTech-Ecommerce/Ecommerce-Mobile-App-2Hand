package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpActivity extends AppCompatActivity {

    Button btnContinue;
    ImageButton btnReturn;
    TextView txtResetPassword;
    FirebaseAuth auth;

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

        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        createAccount();
        returnToSignIn();
        resetPassword();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private boolean isFieldNotEmpty(String field) {
        return !field.isEmpty();
    }

    private boolean isAlphabet(String text) {
        return text.matches("[a-zA-Z]+");
    }

    private boolean isValidName(String text) {
        return text.length() >= 2;
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

                if (!isFieldNotEmpty(firstName)) {
                    ((EditText) findViewById(R.id.firstname)).setError("cannot be blank");
                    return;
                }

                if (!isAlphabet(firstName)) {
                    ((EditText) findViewById(R.id.firstname)).setError("Invalid");
                    return;
                }

                if (!isValidName(firstName)) {
                    ((EditText) findViewById(R.id.firstname)).setError("Must have at least 2 characters");
                    return;
                }

                if (!isFieldNotEmpty(lastName)) {
                    ((EditText) findViewById(R.id.lastname)).setError("cannot be blank");
                    return;
                }

                if (!isAlphabet(lastName)) {
                    ((EditText) findViewById(R.id.lastname)).setError("Không hợp lệ");
                    return;
                }

                if (!isValidName(lastName)) {
                    ((EditText) findViewById(R.id.lastname)).setError("Must have at least 2 characters");
                    return;
                }

                if (!isFieldNotEmpty(email)) {
                    ((EditText) findViewById(R.id.email_address)).setError("Email cannot be blank");
                    return;
                }

                if (!isValidEmail(email)) {
                    ((EditText) findViewById(R.id.email_address)).setError("Email invalid");
                    return;
                }

                if (!isFieldNotEmpty(password)) {
                    ((EditText) findViewById(R.id.password)).setError("Password cannot be blank");
                    return;
                }

                if (!isValidPassword(password)) {
                    ((EditText) findViewById(R.id.password)).setError("Password must have at least 6 characters");
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SignUpActivity.this, OnboardingActivity.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("firstName", firstName);
                                    intent.putExtra("lastName", lastName);
                                    String displayName = firstName + " " + lastName;
                                    intent.putExtra("displayName", displayName);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(SignUpActivity.this, "Signup successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
}
