package com.example.ecommercemobileapp2hand.Views.Settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.WishList;
import com.example.ecommercemobileapp2hand.R;

import java.util.List;

public class WishListAdapter  extends RecyclerView.Adapter<WishListAdapter.WishlistViewHolder>{
    private List<WishList> wishlistItems;

    public WishListAdapter(List<WishList> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    @Override
    public WishlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WishlistViewHolder holder, int position) {
        WishList item = wishlistItems.get(position);
        holder.tvNameWish.setText(item.getName());
        holder.tvQuantity.setText(item.getQuantity() + " Products");
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameWish;
        TextView tvQuantity;
        ImageView iconHearth;
        ImageView iconArrowRight;

        public WishlistViewHolder(View itemView) {
            super(itemView);
            tvNameWish = itemView.findViewById(R.id.tv_name_wish);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            iconHearth = itemView.findViewById(R.id.icon_hearth);
            iconArrowRight = itemView.findViewById(R.id.icon_arrow_right);
        }
    }
}
