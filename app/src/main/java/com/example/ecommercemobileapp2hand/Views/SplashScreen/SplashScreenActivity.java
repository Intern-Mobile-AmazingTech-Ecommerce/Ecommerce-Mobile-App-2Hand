package com.example.ecommercemobileapp2hand.Views.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Login.SignInActivity;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    UserAccountHandler userAccountHandler = new UserAccountHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Handler().postDelayed(() -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                // đăng nhập với firebase
                String email = firebaseUser.getEmail();
                if (email != null && !email.isEmpty()) {
                    // kiểm tra
                    userAccountHandler.checkEmailExists(email, new UserAccountHandler.Callback<Boolean>() {
                        @Override
                        public void onResult(Boolean result) {
                            if (result) {
                                UserAccountHandler.getUserAccount(email, new UserAccountHandler.Callback<UserAccount>() {
                                    @Override
                                    public void onResult(UserAccount result) {
                                        UserAccount userAccount = result;
                                        runOnUiThread(()->{
                                            UserAccountManager.getInstance().setCurrentUserAccount(userAccount);
                                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                        });
                                    }
                                });

                            } else {firebaseAuth.signOut();
                                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                            }
                        }
                    });
                } else {
                    firebaseAuth.signOut();
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                }
            } else {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
            }
            finish();
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
