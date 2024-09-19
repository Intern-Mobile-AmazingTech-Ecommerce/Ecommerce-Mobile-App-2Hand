package com.example.ecommercemobileapp2hand.Views.Checkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.UserCardsHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserCards;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.CardAdapter;
import com.example.ecommercemobileapp2hand.Views.Settings.AddCardActivity;
import com.example.ecommercemobileapp2hand.Views.Settings.PaymentActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChooseCardActivity extends AppCompatActivity {
    ExecutorService service = Executors.newSingleThreadExecutor();
    private ImageView imgBack;
    private RecyclerView recy_cards;
    private CardView cv_cards;
    private ArrayList<UserCards> lstCard;
    private CardAdapter cardAdapter;
    private int cardID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        getCardID();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvents();
        loadListCard();
    }

    private void addControls()
    {
        imgBack = findViewById(R.id.imgBack);
        recy_cards = findViewById(R.id.recy_cards);
        cv_cards = findViewById(R.id.cv_cards);

    }
    private void addEvents()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cv_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseCardActivity.this, AddCardActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getCardID(){
        Intent intent=getIntent();
        cardID=intent.getIntExtra("cardID",0);
    }
    void loadListCard() {
        service.execute(() -> {
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
//            String email = sharedPreferences.getString("userEmail", "");
//            UserAccount user = UserAccountHandler.getUserAccountByEmail(email);
            UserAccount user = UserAccountManager.getInstance().getCurrentUserAccount();
            if (user != null) {
                String userId = user.getUserId();
                UserCardsHandler.getListCardByUserId(userId, new UserCardsHandler.Callback<ArrayList<UserCards>>() {
                    @Override
                    public void onResult(ArrayList<UserCards> result) {
                        lstCard =result;
                        if (lstCard != null && !lstCard.isEmpty()) {
                            runOnUiThread(() -> {
                                cardAdapter = new CardAdapter(lstCard, ChooseCardActivity.this, R.layout.custom_item_choose_card, new CardAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("selected_item", position);
                                        setResult(RESULT_OK, resultIntent);
                                        finish();
                                    }
                                });
                                if (cardID>0){
                                    cardAdapter.setCardID(cardID);
                                }
                                recy_cards.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recy_cards.setAdapter(cardAdapter);
                            });
                        }
                        else {
                            runOnUiThread(() -> {
                            });
                        }
                    }
                });
            }
        });
    }
}