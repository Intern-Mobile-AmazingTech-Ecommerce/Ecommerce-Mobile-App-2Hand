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

public class SortByAdapter extends RecyclerView.Adapter<SortByAdapter.MyViewHoler>
{
    private ArrayList<String> lstSortBy;
    private Context context;
    private String checkSortBy;
    private OnSortBySelectedListener listener;

    public SortByAdapter(ArrayList<String> lstSortBy, Context context, OnSortBySelectedListener listener, String checkSortBy) {
        this.lstSortBy = lstSortBy;
        this.context = context;
        this.listener = listener;
        this.checkSortBy = checkSortBy;
    }
    @NonNull
    @Override
    public SortByAdapter.MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sortby,parent,false);
        return new SortByAdapter.MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SortByAdapter.MyViewHoler holder, int position)
    {
        String g = lstSortBy.get(position);
        holder.tvSortBy.setText(g);
        if (g.equals(checkSortBy))
        {
            holder.imgCheck.setVisibility(View.VISIBLE);
            holder.imgCheck.setImageResource(R.drawable.check_line);
            holder.tvSortBy.setText(g);
            holder.tvSortBy.setTextColor(Color.WHITE);
            holder.linear_sortBy.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
        }else {
            holder.imgCheck.setVisibility(View.GONE);
            holder.tvSortBy.setText(g);
        }
        holder.linear_sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSortBy = g;
                listener.onSortBySelected(g);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return lstSortBy.size();
    }

    public interface OnSortBySelectedListener{
        void onSortBySelected(String selectedSortBy);
    }



    public class MyViewHoler extends RecyclerView.ViewHolder
    {
        private RelativeLayout linear_sortBy;
        private TextView tvSortBy;
        private ImageView imgCheck;
        public MyViewHoler(@NonNull View itemView) {
            super(itemView);
            this.linear_sortBy = itemView.findViewById(R.id.linear_SortBy);
            this.tvSortBy = itemView.findViewById(R.id.tvSortBy);
            this.imgCheck = itemView.findViewById(R.id.imgCheck);
        }
    }
}
