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

import com.example.ecommercemobileapp2hand.Models.ProductDetailsSize;
import com.example.ecommercemobileapp2hand.Models.Size;
import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;

public class RecycleSizeAdapter extends RecyclerView.Adapter<RecycleSizeAdapter.MyViewHolder> {

    private ArrayList<ProductDetailsSize> sizes;
    private Context context;
    private OnSizeSelectedListener listener;
    private ProductDetailsSize selectedSize;

    public RecycleSizeAdapter(ArrayList<ProductDetailsSize> sizes, Context context, OnSizeSelectedListener listener, ProductDetailsSize selectedSize) {
        this.sizes = sizes;
        this.context = context;
        this.listener = listener;
        this.selectedSize = selectedSize;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sortby,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductDetailsSize size = sizes.get(position);
        holder.tvSize.setText(size.getSize().getSize_name());
        if(size.getStock() != 0){
            if(size.getSize().getSize_name().contains(selectedSize.getSize().getSize_name())){
                holder.checked.setVisibility(View.VISIBLE);
                holder.checked.setImageResource(R.drawable.check_line);
                holder.tvSize.setTextColor(Color.WHITE);
                holder.linear_layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
            }else {
                holder.checked.setVisibility(View.GONE);
            }
            holder.linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedSize = size;
                    listener.onSizeSelected(selectedSize);
                    notifyDataSetChanged();
                }
            });
            holder.linear_layout.setClickable(true);
        }else {
            holder.linear_layout.setClickable(false);
            holder.linear_layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BCBCBC")));
        }

    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout linear_layout;
        private TextView tvSize;
        private ImageView checked;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linear_layout = itemView.findViewById(R.id.linear_SortBy);
            tvSize = itemView.findViewById(R.id.tvSortBy);
            checked = itemView.findViewById(R.id.imgCheck);
        }
    }

    public interface OnSizeSelectedListener{
        void onSizeSelected(ProductDetailsSize size);
    }
}
