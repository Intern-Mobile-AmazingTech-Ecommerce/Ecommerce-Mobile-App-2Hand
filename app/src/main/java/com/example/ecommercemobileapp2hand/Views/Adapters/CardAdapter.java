package com.example.ecommercemobileapp2hand.Views.Adapters;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.UserCards;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Settings.PaymentActivity;
import com.example.ecommercemobileapp2hand.Views.Settings.UpdateCardActivity;

import java.util.List;

import androidx.annotation.NonNull;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<UserCards> cardList;
    private Context context;

    public CardAdapter(List<UserCards> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        UserCards card = cardList.get(position);
        String cardNumber = card.getUser_card_number().substring(card.getUser_card_number().length()-4);
        holder.cardNumberTextView.setText("**** "+cardNumber);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateCardActivity.class);
                intent.putExtra("id",card.getUser_cards_id());
                intent.putExtra("cardNumber",card.getUser_card_number());
                intent.putExtra("ccv",card.getUser_card_ccv());
                intent.putExtra("exp",card.getUser_card_exp());
                intent.putExtra("hold",card.getUser_card_holder_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList!=null ? cardList.size() : 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumberTextView;


        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumberTextView = itemView.findViewById(R.id.tvCardNumber);

        }
    }
}