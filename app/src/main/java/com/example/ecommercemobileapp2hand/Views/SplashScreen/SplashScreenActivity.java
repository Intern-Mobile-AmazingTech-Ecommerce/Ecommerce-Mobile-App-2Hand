package com.example.ecommercemobileapp2hand.Views.SplashScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.ecommercemobileapp2hand.Views.Login.SignInActivity;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private UserAccount userAccount;

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

//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("UserAccount")) {
////            userAccount = (UserAccount) intent.getSerializableExtra("UserAccount");
//        }

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
            String email = getEmailFromSharedPreferences();

            if (isLoggedIn && email != null) {
                UserAccountHandler.getUserAccount(email, new UserAccountHandler.Callback<UserAccount>() {
                    @Override
                    public void onResult(UserAccount result) {
                        runOnUiThread(()->{
                            if(result != null){
                                userAccount = result;
                                UserAccountManager.getInstance().setCurrentUserAccount(userAccount);
                                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                mainIntent.putExtra("email", email);
                                startActivity(mainIntent);
                                finish();
                            }else {
                                Toast.makeText(SplashScreenActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

            } else {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                finish();
            }

        }, 1000);
    }

    private String getEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }
}