package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

public class SignInPasswordActivity extends AppCompatActivity {

    Button btnContinue_2;
    ImageButton btnReturn;
    TextView txtResetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        signIn();
        resetPassword();
        returnToSignIn();
    }
    private void signIn(){
        btnContinue_2=(Button) findViewById(R.id.btnContinue_2);
        btnContinue_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInPasswordActivity.this, MainActivity.class));
                finish(); //không quay lại SignInActivity khi nhấn nút back
                Toast.makeText(SignInPasswordActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void resetPassword(){
        txtResetPassword=(TextView) findViewById(R.id.dont_have_a);
        txtResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInPasswordActivity.this, ForgotPasswordActivity.class));
            }
        });
    }
    private void returnToSignIn(){
        btnReturn=(ImageButton) findViewById(R.id.btn_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}