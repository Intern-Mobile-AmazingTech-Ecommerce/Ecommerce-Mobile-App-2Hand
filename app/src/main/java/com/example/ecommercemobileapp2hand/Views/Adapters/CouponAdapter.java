package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommercemobileapp2hand.Models.Coupon;
import com.example.ecommercemobileapp2hand.R;
import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {

    private List<Coupon> couponList;
    private OnItemClickListener onItemClickListener;

    public CouponAdapter(List<Coupon> couponList, OnItemClickListener onItemClickListener) {
        this.couponList = couponList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon, parent, false);
        return new CouponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {
        Coupon coupon = couponList.get(position);
        holder.tvCouponCode.setText(coupon.getCode());
        String discountText = coupon.getDiscountValue().toString() + "%";
        holder.tvCouponDiscount.setText(discountText);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(coupon));
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    static class CouponViewHolder extends RecyclerView.ViewHolder {
        TextView tvCouponCode, tvCouponDiscount;

        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCouponCode = itemView.findViewById(R.id.tvCouponCode);
            tvCouponDiscount = itemView.findViewById(R.id.tvCouponDiscount);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Coupon coupon);
    }
}