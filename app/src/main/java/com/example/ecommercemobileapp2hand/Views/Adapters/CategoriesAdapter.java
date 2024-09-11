package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.FakeModels.Category;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Homepage.CategoryProductActivity;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private ExecutorService service = Executors.newCachedThreadPool();
    private Future<?> currentTask;
    private ArrayList<ProductCategory> categories;
    private Context context;
    private int layout;
    public CategoriesAdapter(ArrayList<ProductCategory> categories, Context context, int layout) {
        this.categories = categories;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductCategory category = categories.get(position);
        holder.textViewCategoryName.setText(category.getProduct_category_name());
        currentTask = service.submit(()->{
            String imgUrl = Util.getCloudinaryImageUrl(context,category.getProduct_category_thumbnail(),592,592);
            ((android.app.Activity)context).runOnUiThread(()->{
               Picasso.get().load(imgUrl).into(holder.imageViewCategoryIcon);

            });
        });
        holder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Category", category);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relative_layout;
        public TextView textViewCategoryName;
        public ImageView imageViewCategoryIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            relative_layout = itemView.findViewById(R.id.relative_layout);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            imageViewCategoryIcon = itemView.findViewById(R.id.imageViewCategoryIcon);
        }
    }
}
