package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Models.ProductDetailsImg;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RecycleProductImageAdapter extends RecyclerView.Adapter<RecycleProductImageAdapter.MyViewHolder> {

    private ArrayList<ProductDetailsImg> imgLists;
    private Context context;
    private ExecutorService service = Executors.newCachedThreadPool();
    private Map<Integer, Future<?>> taskMap = new HashMap<>();

    public RecycleProductImageAdapter(ArrayList<ProductDetailsImg> imgLists, Context context) {
        this.imgLists = imgLists;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_img_product_slider, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Future<?> previousTask = taskMap.get(holder.getAdapterPosition());
        if (previousTask != null) {
            previousTask.cancel(true);
        }

        Future<?> currentTask = service.submit(() -> {
            ProductDetailsImg img = imgLists.get(position);
            String url = Util.getCloudinaryImageUrl(context, img.getImg_url(), 592, 592);
            ((android.app.Activity) context).runOnUiThread(() -> {
                if (holder.getAdapterPosition() == position) {
                    Picasso.get().load(url).into(holder.imgProduct);
                }
            });
        });

        taskMap.put(holder.getAdapterPosition(), currentTask);

    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        Future<?> task = taskMap.remove(holder.getAdapterPosition());
        if (task != null) {
            task.cancel(true); // Hủy tác vụ hiện tại
        }
    }

    @Override
    public int getItemCount() {
        return imgLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductSliderItem);
        }
    }
}
