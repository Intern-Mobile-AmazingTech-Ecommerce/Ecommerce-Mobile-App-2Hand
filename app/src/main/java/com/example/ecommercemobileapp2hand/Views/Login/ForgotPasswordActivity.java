package com.example.ecommercemobileapp2hand.Views.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtEmail;
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
        initialUI();
        sendEmailReset();
        returnToSignInPassword();
    }

    private void initialUI() {
        btnContinue_2 = findViewById(R.id.btnContinue_2);
        edtEmail = findViewById(R.id.email_address);
        btnContinue_2.setOnClickListener(v -> {
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

    private void sendEmailReset()
    {
        ResetPassword();
    }

    private void ResetPassword(){
        btnContinue_2 = findViewById(R.id.btnContinue_2);
        String strEmail = "duongvankhuong197@gmail.com";
        FirebaseAuth auth = FirebaseAuth.getInstance();
        btnContinue_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(strEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(ForgotPasswordActivity.this, returnlogin.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
    private void returnToSignInPassword(){
        btnReturn=(ImageButton) findViewById(R.id.btn_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                startActivity(myintent);
            }
        });
    }
}