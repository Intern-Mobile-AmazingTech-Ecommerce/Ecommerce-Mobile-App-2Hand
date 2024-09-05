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

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.MyViewHolder>
{
    ArrayList<String> lstDeal;
    Context context;
    String checkDeal;
    OnDealSelectedListener listener;
    public interface OnDealSelectedListener{
        void onDealSelected(String selectedGender);
    }

    public DealAdapter(ArrayList lstDeal, Context context, String checkDeal, OnDealSelectedListener listener) {
        this.lstDeal = lstDeal;
        this.context = context;
        this.checkDeal = checkDeal;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DealAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_deal,parent,false);
        return new DealAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DealAdapter.MyViewHolder holder, int position)
    {
        String g = lstDeal.get(position);
        holder.tvDeal.setText(g);
        if (g.equals(checkDeal))
        {
            holder.imgCheck.setVisibility(View.VISIBLE);
            holder.imgCheck.setImageResource(R.drawable.check_line);
            holder.imgCheck.setBackgroundResource(R.drawable.circle_completed);
            holder.tvDeal.setText(g);
            holder.tvDeal.setTextColor(Color.WHITE);
            holder.linear_deal.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
        }else {
            holder.imgCheck.setVisibility(View.GONE);
            holder.tvDeal.setText(g);
        }
        holder.linear_deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDeal = g;
                listener.onDealSelected(g);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstDeal.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout linear_deal;
        private TextView tvDeal;
        private ImageView imgCheck;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.linear_deal = itemView.findViewById(R.id.linear_Deal);
            this.tvDeal = itemView.findViewById(R.id.tvDeal);
            this.imgCheck = itemView.findViewById(R.id.imgCheck);
        }
    }
}
