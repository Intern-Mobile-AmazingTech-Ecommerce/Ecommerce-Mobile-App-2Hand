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

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

public class SignInPasswordActivity extends AppCompatActivity {

    Button btnContinue;
    TextView txtResetPassword;
    EditText edtpassword;
    UserAccount userAccount;
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
        getIt();
        addControl();

    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent();
    }

    private void addControl() {
        btnContinue = findViewById(R.id.btnContinue_2);
        edtpassword = findViewById(R.id.password);
        txtResetPassword = findViewById(R.id.dont_have_a);
    }

    private void addEvent()
    {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidPassword(edtpassword.getText().toString()))
                {
                    ((EditText) findViewById(R.id.password)).setError("Mật khẩu phải có ít nhất 6 ký tự");
                }
                else
                {
                    UserAccount userAccount1 = UserAccountHandler.getUserAccountById(UserAccountHandler.getUserAccount(userAccount.getEmail(), edtpassword.getText().toString()));
                    if (userAccount1 != null)
                    {
                        Intent intent = new Intent(SignInPasswordActivity.this, MainActivity.class);
                        intent.putExtra("UserAccount", userAccount1);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(SignInPasswordActivity.this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        txtResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(SignInPasswordActivity.this, ForgotPasswordActivity.class);
                startActivity(myintent);
            }
        });
    }
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private void getIt()
    {
        Intent intent = getIntent();
        userAccount = (UserAccount) intent.getSerializableExtra("UserAccount");
    }
}