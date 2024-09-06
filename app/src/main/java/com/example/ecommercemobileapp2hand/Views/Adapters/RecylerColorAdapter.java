package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.ProductColor;
import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;

public class RecylerColorAdapter extends RecyclerView.Adapter<RecylerColorAdapter.MyViewHolder> {

    private ArrayList<ProductColor> productColors;
    private Context context;
    private String checkColor;
    private OnColorsSelectedListener listener;

    public RecylerColorAdapter(ArrayList<ProductColor> productColors, Context context, String checkColor, OnColorsSelectedListener onColorsSelectedListener) {
        this.productColors = productColors;
        this.context = context;
        this.checkColor = checkColor;
        this.listener = onColorsSelectedListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_button,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductColor color = productColors.get(position);
        holder.btnColor.setText(color.getProduct_color_name());
        String colorName = color.getProduct_color_name().toLowerCase().trim();
        int colorValue = -999;
        try{
            if (colorName.contains("dark blue")) {
                colorValue = Color.parseColor("#00008B");
            } else {
                colorValue = Color.parseColor(colorName);
            }
        }catch (IllegalAccessError e){
            colorValue = Color.parseColor("white");
        }

        holder.color.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        if(color.getProduct_color_name().contains(checkColor)){
            holder.btnColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
            holder.btnColor.setTextColor(Color.WHITE);
            holder.checked.setVisibility(View.VISIBLE);
        }else {
            holder.checked.setVisibility(View.GONE);
        }
        holder.btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkColor = color.getProduct_color_name();
                listener.onColorSelected(checkColor);
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return productColors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private Button btnColor;
        private View color;
        private ImageView checked;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnColor = itemView.findViewById(R.id.button_content);
            color = itemView.findViewById(R.id.color);
            checked = itemView.findViewById(R.id.icon_check);
        }
    }

    public interface OnColorsSelectedListener {
        void onColorSelected(String colorName);
    }
}
