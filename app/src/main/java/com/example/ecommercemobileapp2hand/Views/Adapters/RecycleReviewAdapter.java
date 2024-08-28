package com.example.ecommercemobileapp2hand.Views.ProductPage.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.FakeModels.Reviews;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;

import java.util.ArrayList;

public class RecycleReviewAdapter extends RecyclerView.Adapter<RecycleReviewAdapter.MyViewHolder> {

    private ArrayList<Reviews> reviewsList;
    private Context context;

    public RecycleReviewAdapter(ArrayList<Reviews> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_review,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reviews reviews = reviewsList.get(position);
        Bitmap bitmap = Util.convertStringToBitmapFromAccess(context.getApplicationContext(),reviews.getAvt());
        holder.imgAvt.setImageBitmap(bitmap);

        holder.ratingBar.setRating(reviews.getRatingPoint());
        holder.tvReviewContent.setText(reviews.getReviewContent());

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAvt;
        RatingBar ratingBar;
        TextView tvCustomerName,tvReviewContent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.img_Avatar);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvReviewContent = itemView.findViewById(R.id.tv_review);

        }
    }
}
