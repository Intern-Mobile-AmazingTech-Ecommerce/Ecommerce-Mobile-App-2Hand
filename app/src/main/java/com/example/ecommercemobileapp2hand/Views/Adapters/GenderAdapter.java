package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.ProductObject;
import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;

public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.MyViewHolder>{
    ArrayList<ProductObject> lstGender;
    Context context;
    String checkGender;
    OnGenderSelectedListener listener;

    public interface OnGenderSelectedListener {
        void onGenderSelected(String selectedGender);
    }

    public GenderAdapter(ArrayList<ProductObject> lstGender, Context context, String checkGender, OnGenderSelectedListener listener) {
        this.lstGender = lstGender;
        this.context = context;
        this.checkGender = checkGender;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewholder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gender, parent, false);
        return new GenderAdapter.MyViewHolder(viewholder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductObject g = lstGender.get(position);

        holder.tvGender.setText(g.getObject_name());
        if (g.getObject_name().equals(checkGender))
        {
            holder.imgCheck.setVisibility(View.VISIBLE);
            holder.imgCheck.setImageResource(R.drawable.check_line);
            holder.imgCheck.setBackgroundResource(R.drawable.circle_completed);
            holder.tvGender.setText(g.getObject_name());
            holder.tvGender.setTextColor(Color.WHITE);
            holder.linear_gender.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
        }
        else
        {
            holder.imgCheck.setVisibility(View.GONE);
            holder.tvGender.setText(g.getObject_name());
        }
        holder.linear_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGender = g.getObject_name();  // Cập nhật giới tính được chọn
                listener.onGenderSelected(g.getObject_name());  // Gọi callback để thông báo sự thay đổi
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstGender != null ? lstGender.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private RelativeLayout linear_gender;
        private TextView tvGender;
        private ImageView imgCheck;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.linear_gender = itemView.findViewById(R.id.linear_gender);
            this.tvGender = itemView.findViewById(R.id.tvGender);
            this.imgCheck = itemView.findViewById(R.id.imgCheck);
        }
    }
}
