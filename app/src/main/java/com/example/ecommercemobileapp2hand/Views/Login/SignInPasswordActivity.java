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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

public class SignInPasswordActivity extends AppCompatActivity {

    Button btnContinue;
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
        initialUI();
        resetPassword();
    }

    private void initialUI() {
        btnContinue = findViewById(R.id.btnContinue_2);
        EditText password = findViewById(R.id.password);

        btnContinue.setOnClickListener(v -> {
            String pass = password.getText().toString().trim();
            if (!isValidPassword(pass)) {
                ((EditText) findViewById(R.id.password)).setError("Mật khẩu phải có ít nhất 6 ký tự");
                return;
            } else {
                Intent intent = new Intent(SignInPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private boolean isValidPassword(String password) {
        return password.length() >= 6;
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
}