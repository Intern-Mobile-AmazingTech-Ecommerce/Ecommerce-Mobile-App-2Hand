package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Models.FakeModels.Age;
import com.example.ecommercemobileapp2hand.Views.Adapters.AgeAdapter;
import com.example.ecommercemobileapp2hand.Views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private Spinner spiAge;
    private AgeAdapter ageAdapter;
    private Button btnFinish,btn_men,btn_women;
    private String gender="Men";


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
        btn_men=findViewById(R.id.btn_men);
        btn_men.setBackgroundColor(Color.parseColor("#8E6CEF"));
        btn_women=findViewById(R.id.btn_women);
        spiAge= findViewById(R.id.spi_age);
        ageAdapter = new AgeAdapter(this,R.layout.age_selected,getList());
        spiAge.setAdapter(ageAdapter);
//        spiAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this,ageAdapter.getItem(position).getAge(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        // Lấy thông tin cấu hình của thiết bị
        Configuration configuration = getResources().getConfiguration();

        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        btn_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_men.setBackgroundColor(Color.parseColor("#8E6CEF"));

                // Kiểm tra chế độ theme

                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    // Đang ở chế độ Dark Theme
                    btn_women.setBackgroundColor(Color.parseColor("#342f3f"));

                } else if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                    // Đang ở chế độ Light Theme
                    btn_women.setBackgroundColor(Color.parseColor("#f4f4f4"));

                }
                gender="Men";
            }
        });

        btn_women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_women.setBackgroundColor(Color.parseColor("#8E6CEF"));

                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    // Đang ở chế độ Dark Theme
                    btn_men.setBackgroundColor(Color.parseColor("#342f3f"));

                } else if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                    // Đang ở chế độ Light Theme
                    btn_men.setBackgroundColor(Color.parseColor("#f4f4f4"));

                }
                gender="Women";
            }
        });
        btnFinish=(Button) findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tao sharedreferences luu gia tri gender
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("gender_key", gender);
                editor.apply();



                //khoi tao doi tuong
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    // Extract data from the bundle
                    String firstName = bundle.getString("firstName");
                    String lastName = bundle.getString("lastName");
                    String email = bundle.getString("email");
                    String password = bundle.getString("password");
                    UserAccount userAccount=new UserAccount(-1,"",password,gender,email,"",firstName,lastName, "");
                    //viet handler them user_account
                }
                startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    private List<Age> getList()
    {
        List<Age> list= new ArrayList<>();
        list.add(new Age("Age range"));
        list.add(new Age("6->10"));
        list.add(new Age("11->15"));
        list.add(new Age("16->20"));
        list.add(new Age("21->30"));
        return list;
    }
}