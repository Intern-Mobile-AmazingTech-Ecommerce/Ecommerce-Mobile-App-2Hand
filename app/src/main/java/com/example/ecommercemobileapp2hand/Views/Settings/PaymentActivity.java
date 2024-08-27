package com.example.ecommercemobileapp2hand.Views.Settings;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.FakeModels.Card;
import com.example.ecommercemobileapp2hand.Models.FakeModels.Paypal;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Settings.CustomAdapters.CardAdapter;
import com.example.ecommercemobileapp2hand.Views.Settings.CustomAdapters.PaypalAdapter;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView rv_paypal;
    private PaypalAdapter adapter;
    private List<Paypal> paypalList;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<Card> cardList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
        recyclerView = findViewById(R.id.rv_Cards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //fake data
        cardList = new ArrayList<>();
        cardList.add(new Card("**** 4187", "123", "12/25", "John Doe"));
        cardList.add(new Card("**** 9387", "456", "11/24", "Jane Smith"));

        // Set up the adapter
        cardAdapter = new CardAdapter(cardList);
        recyclerView.setAdapter(cardAdapter);


        rv_paypal = findViewById(R.id.rv_Paypal);
        rv_paypal.setLayoutManager(new LinearLayoutManager(this));

// Initialize the list
        paypalList = new ArrayList<>();

// Add sample data
        paypalList.add(new Paypal("Cloth@gmail.com"));


// Set up the adapter with the list
        adapter = new PaypalAdapter(paypalList);
        rv_paypal.setAdapter(adapter);
    }
}