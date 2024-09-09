package com.example.ecommercemobileapp2hand.Views.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.ListAddressViewAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ListAddressActivity extends AppCompatActivity {

    private ImageButton btn_return;
    private ListView listAddress;
    private ArrayList<String> arrayList=new ArrayList<>();
    private ListAddressViewAdapter adapter;
    Connection connection;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //
        addControl();
        viewAddress();
        returnToSettings();

    }
    private void addControl(){
        listAddress=(ListView) findViewById(R.id.listAddress);
    }

    private void viewAddress(){
        DBConnect c = new DBConnect();
        connection = c.connectionClass();
        if(c != null){
            try {
                String sqlstatement = "Select user_address_city from user_address";
                Statement smt = connection.createStatement();
                ResultSet set = smt.executeQuery(sqlstatement);
                while (set.next()){
                    arrayList.add(set.getString(1));
                }
                connection.close();
            }catch (Exception e){
                Log.e( "Error:",e.getMessage());
            }
        }
        adapter=new ListAddressViewAdapter(ListAddressActivity.this,R.layout.custom_list_address,arrayList);
        listAddress.setAdapter(adapter);
        returnToSettings();
    }

    private void returnToSettings(){
        btn_return=(ImageButton) findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}