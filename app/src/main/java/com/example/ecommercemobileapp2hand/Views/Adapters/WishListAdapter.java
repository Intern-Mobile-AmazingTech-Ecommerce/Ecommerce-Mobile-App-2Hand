package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.Wishlist;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Settings.WishlistDetail;

import java.util.List;

public class WishListAdapter  extends RecyclerView.Adapter<WishListAdapter.WishlistViewHolder>{
    private List<Wishlist> wishlistItems;
    private Context context;
    private int is_product_added;
    public WishListAdapter(Context context,List<Wishlist> wishlistItems) {
        this.wishlistItems = wishlistItems;
        this.context = context;
    }
    public WishListAdapter(Context context,List<Wishlist> wishlistItems,int product_details_id) {
        this.wishlistItems = wishlistItems;
        this.context = context;
        this.is_product_added = product_details_id;
    }
    @Override
    public WishlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WishlistViewHolder holder, int position) {
        Wishlist item = wishlistItems.get(position);
        holder.tvNameWish.setText(item.getWishlist_name());
        holder.tvQuantity.setText(item.getWishlist_quantity() + " Products");
        holder.wishListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WishlistDetail.class);
                intent.putExtra("wishlistID",item.getWishlist_id());
                context.startActivity(intent);
            }
        });
        if(is_product_added != -1){
            holder.iconArrowRight.setVisibility(View.GONE);
            holder.cbAdded.setVisibility(View.VISIBLE);
        }else {
            holder.iconArrowRight.setVisibility(View.VISIBLE);
            holder.cbAdded.setVisibility(View.GONE);
        }

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
        ConstraintLayout wishListItem;
        CheckBox cbAdded;
        public WishlistViewHolder(View itemView) {
            super(itemView);
            cbAdded = itemView.findViewById(R.id.cbAdded);
            tvNameWish = itemView.findViewById(R.id.tv_name_wish);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            iconHearth = itemView.findViewById(R.id.icon_hearth);
            iconArrowRight = itemView.findViewById(R.id.icon_arrow_right);
            wishListItem = itemView.findViewById(R.id.wishList_item);
        }
    }
}
