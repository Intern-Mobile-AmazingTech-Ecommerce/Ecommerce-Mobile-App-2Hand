package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.MyViewHolder> {

    ArrayList<String> lstPrice;
    Context context;
    String checkPrice;
    OnPriceSelectedListener listener;
    public interface OnPriceSelectedListener{
        void onPriceSelected(String selectedPricce);
    }

    public PriceAdapter(ArrayList<String> lstPrice, Context context, String checkPrice, OnPriceSelectedListener listener) {
        this.lstPrice = lstPrice;
        this.context = context;
        this.checkPrice = checkPrice;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_deal,parent,false);
        return new PriceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String g = lstPrice.get(position);
        holder.tvPrice.setText(g);
        if (g.equals(checkPrice))
        {
            holder.imgCheck.setVisibility(View.VISIBLE);
            holder.imgCheck.setImageResource(R.drawable.check_line);
            holder.imgCheck.setBackgroundResource(R.drawable.circle_completed);
            holder.tvPrice.setText(g);
            holder.tvPrice.setTextColor(Color.WHITE);
            holder.linear_Price.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
        }else
        {
            holder.imgCheck.setVisibility(View.GONE);
            holder.tvPrice.setText(g);
        }
        holder.linear_Price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPrice = g;
                listener.onPriceSelected(g);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstPrice.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout linear_Price;
        private TextView tvPrice;
        private ImageView imgCheck;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.linear_Price = itemView.findViewById(R.id.linear_Price);
            this.tvPrice = itemView.findViewById(R.id.tvPrice);
            this.imgCheck = itemView.findViewById(R.id.imgCheck);
        }
    }
}
