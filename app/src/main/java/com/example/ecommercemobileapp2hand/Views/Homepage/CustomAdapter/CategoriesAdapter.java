package com.example.ecommercemobileapp2hand.Views.Homepage.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.Category;
import com.example.ecommercemobileapp2hand.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Category> categories;
    private Context context;
    private int layout;
    public CategoriesAdapter(List<Category> categories, Context context, int layout) {
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
        Category category = categories.get(position);
        holder.textViewCategoryName.setText(category.getName());
        holder.imageViewCategoryIcon.setImageResource(category.getIconResId());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCategoryName;
        public ImageView imageViewCategoryIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            imageViewCategoryIcon = itemView.findViewById(R.id.imageViewCategoryIcon);
        }
    }
}
