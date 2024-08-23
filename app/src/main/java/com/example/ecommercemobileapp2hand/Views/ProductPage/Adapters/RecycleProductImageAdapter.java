package com.example.ecommercemobileapp2hand.Views.ProductPage.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;

import java.util.ArrayList;

public class RecycleProductImageAdapter extends RecyclerView.Adapter<RecycleProductImageAdapter.MyViewHolder> {

    private ArrayList<String> imgLists;
    private Context context;

    public RecycleProductImageAdapter(ArrayList<String> imgLists, Context context) {
        this.imgLists = imgLists;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_img_product_slider,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String img = imgLists.get(position);
        Bitmap bitmap = Util.convertStringToBitmapFromAccess(context,img);
        holder.imgProduct.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return imgLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imgProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductSliderItem);
        }
    }
}
