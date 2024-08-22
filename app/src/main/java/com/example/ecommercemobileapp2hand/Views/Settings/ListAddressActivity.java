package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Settings.CustomAdapters.ListAddressViewAdapter;

import java.util.ArrayList;

public class ListAddressActivity extends AppCompatActivity {

    private ImageButton btn_return;
    private ListView listAddress;
    private ArrayList<String> arrayList=new ArrayList<>();
    private ListAddressViewAdapter adapter;
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
        listAddress=(ListView) findViewById(R.id.listAddress);
        arrayList.add(new String("469/32 Nguyễn Kiệm"));
        arrayList.add(new String("468/32 Nguyễn Kiệm"));
        adapter=new ListAddressViewAdapter(getApplicationContext(),R.layout.custom_list_address,arrayList);
        listAddress.setAdapter(adapter);
        returnToSettings();

    }
    private void returnToSettings(){
        btn_return=(ImageButton) findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}