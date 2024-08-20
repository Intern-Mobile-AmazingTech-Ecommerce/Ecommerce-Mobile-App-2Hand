package com.example.ecommercemobileapp2hand.Views.Homepage.CustomAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.MyViewHolder> {
    ArrayList<Product> lstPro;
    Context context;

    public ProductCardAdapter(ArrayList<Product> lstPro, Context context) {
        this.lstPro = lstPro;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewholder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_productcard, parent, false);
        return new MyViewHolder(viewholder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product pro = lstPro.get(position);

        int resourceid = holder.itemView.getContext().getResources().getIdentifier(pro.getThumbnail(), "drawable", holder.itemView.getContext().getPackageName());
        holder.img_Product.setImageResource(resourceid);

        holder.tvProductName.setText(pro.getProduct_name().substring(0, 22) + "...");

        if (pro.getSale_price() == 0)
        {
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvSalePrice.setText("$" + String.valueOf(pro.getBase_price()) + ".00");
        }
        else
        {
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvSalePrice.setText("$" + String.valueOf(pro.getSale_price()) + ".00");
            holder.tvPrice.setText("$" + String.valueOf(pro.getBase_price()) + ".00");

            // Gạch ngang giá gốc
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return lstPro.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView img_Product, img_Heart;
        private TextView tvProductName, tvSalePrice, tvPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img_Product = itemView.findViewById(R.id.img_Product);
            this.img_Heart = itemView.findViewById(R.id.img_Heart);
            this.tvProductName = itemView.findViewById(R.id.tvProductName);
            this.tvSalePrice = itemView.findViewById(R.id.tvSalePrice);
            this.tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
