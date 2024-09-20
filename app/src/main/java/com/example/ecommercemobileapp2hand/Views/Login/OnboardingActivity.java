package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Models.FakeModels.Age;
import com.example.ecommercemobileapp2hand.Views.Adapters.AgeAdapter;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnboardingActivity extends AppCompatActivity {
    private Spinner spiAge;
    private AgeAdapter ageAdapter;
    private Button btnFinish, btn_men, btn_women;
    private String gender = "Men";
    private String ageRange = "";
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControl();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void addControl() {
        btn_men = findViewById(R.id.btn_men);
        btn_women = findViewById(R.id.btn_women);
        btnFinish = findViewById(R.id.btn_finish);
        spiAge = findViewById(R.id.spi_age);
    }

    public void addEvents() {
        btn_men.setBackgroundColor(Color.parseColor("#8E6CEF"));

        ageAdapter = new AgeAdapter(this, R.layout.age_selected, getList());
        spiAge.setAdapter(ageAdapter);

        Configuration configuration = getResources().getConfiguration();
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        btn_men.setOnClickListener(v -> {
            gender = "Men";
            btn_men.setBackgroundColor(Color.parseColor("#8E6CEF"));
            updateButtonColor(btn_women, currentNightMode);
        });

        btn_women.setOnClickListener(v -> {
            gender = "Women";
            btn_women.setBackgroundColor(Color.parseColor("#8E6CEF"));
            updateButtonColor(btn_men, currentNightMode);
        });

        btnFinish.setOnClickListener(v -> {
            int selectedPosition = spiAge.getSelectedItemPosition();
            ageRange = ageAdapter.getItem(selectedPosition).getAge();

            if (selectedPosition == 0) {
                Toast.makeText(OnboardingActivity.this, "\n" + "Please select age", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = getIntent();
            if (intent != null) {
                String email = intent.getStringExtra("email");
                String displayName = intent.getStringExtra("displayName");

                if (displayName != null) {
                    String[] nameParts = displayName.split(" ", 2);
                    String firstName = nameParts.length > 0 ? nameParts[0] : "";
                    String lastName = nameParts.length > 1 ? nameParts[1] : "";

                    UserAccountHandler.saveUserToDB(firstName, lastName, email, gender, ageRange);
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("gender_key", gender);
                    editor.putString("age_range_key", ageRange);
                    editor.putBoolean("onboardingCompleted", true);
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("email", email);
                    editor.apply();
                    Intent mainIntent = new Intent(OnboardingActivity.this, MainActivity.class);
                    mainIntent.putExtra("email", email);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Log.e("OnboardingActivity", "Display name is null");
                }
            }
        });
    }

    private List<Age> getList() {
        List<Age> list = new ArrayList<>();
        list.add(new Age("Age Range"));
        list.add(new Age("6-10"));
        list.add(new Age("11-15"));
        list.add(new Age("16-20"));
        list.add(new Age("21-30"));
        return list;
    }

    private void updateButtonColor(Button button, int currentNightMode) {
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            button.setBackgroundColor(Color.parseColor("#342f3f"));
        } else {
            button.setBackgroundColor(Color.parseColor("#f4f4f4"));
        }
    }
}