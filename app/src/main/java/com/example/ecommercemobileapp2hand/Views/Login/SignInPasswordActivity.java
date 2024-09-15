package com.example.ecommercemobileapp2hand.Views.Login;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInPasswordActivity extends AppCompatActivity {

    Button btnContinue;
    TextView txtResetPassword;
    EditText edtPassword;
    UserAccount userAccount;
    String email;
    ImageButton btnReturn;
    private FirebaseAuth firebaseAuth;
    SignInActivity signInActivity;
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
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

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIt();
        addControl();
        addEvent();
        returnToSignIn();
    }

    private void addControl() {
        btnContinue = findViewById(R.id.btnContinue_2);
        edtPassword = findViewById(R.id.password);
        txtResetPassword = findViewById(R.id.dont_have_a);
        btnReturn = findViewById(R.id.btn_return);
    }

    private void addEvent() {
        email = getIntent().getStringExtra("Email");

        btnContinue.setOnClickListener(v -> {
            String password = edtPassword.getText().toString();

            if (!isValidPassword(password)) {
                edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                return;
            }

            if (email != null) {
                new Handler().postDelayed(() -> {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                showProgress(true);
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SignInPasswordActivity.this, MainActivity.class);
                                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                    editor.apply();

                                    UserAccountHandler.getUserAccount(email, new UserAccountHandler.Callback<UserAccount>() {
                                        @Override
                                        public void onResult(UserAccount result) {
                                            UserAccount userAccount = result;
                                            runOnUiThread(()->{
                                                UserAccountManager.getInstance().setCurrentUserAccount(userAccount);
                                                //truyền thông tin user qua MainActivity
                                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                                if (user != null) {
                                                    signInActivity.onLoginSuccess(user.getEmail());
                                                    intent.putExtra("UserAccount", userAccount);
                                                    intent.putExtra("email", user.getEmail());
                                                    intent.putExtra("displayName", user.getDisplayName());
                                                    intent.putExtra("user_id",user.getUid());
                                                }

                                                startActivity(intent);
                                                finish();
                                                Toast.makeText(SignInPasswordActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                            });
                                        }
                                    });

                                } else {
                                    showProgress(false);
                                    showDialog("Đăng nhập thất bại", "Email hoặc mật khẩu không hợp lệ");
                                }
                            });
                }, 3000);
            }
        });

        txtResetPassword.setOnClickListener(v -> {
            Intent myIntent = new Intent(SignInPasswordActivity.this, ForgotPasswordActivity.class);
            startActivity(myIntent);
        });
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void getIt() {
        Intent intent = getIntent();
        userAccount = (UserAccount) intent.getSerializableExtra("UserAccount");
    }

    private void showProgress(boolean show) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void returnToSignIn() {
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInPasswordActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}
