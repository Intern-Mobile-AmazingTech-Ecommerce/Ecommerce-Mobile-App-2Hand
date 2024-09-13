package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.FakeModels.Paypal;
import com.example.ecommercemobileapp2hand.R;

import java.util.List;

public class PaypalAdapter extends RecyclerView.Adapter<PaypalAdapter.PaypalViewHolder> {

    private List<Paypal> paypalList;

    public PaypalAdapter(List<Paypal> paypalList) {
        this.paypalList = paypalList;
    }

    @NonNull
    @Override
    public PaypalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_paypal, parent, false);
        return new PaypalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaypalViewHolder holder, int position) {
        Paypal paypal = paypalList.get(position);
        holder.tvGmail.setText(paypal.getGmail());
    }

    @Override
    public int getItemCount() {
        return paypalList != null ? paypalList.size() : 0;
    }

    public static class PaypalViewHolder extends RecyclerView.ViewHolder {
        TextView tvGmail;

        public PaypalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGmail = itemView.findViewById(R.id.tvGmail);
        }
    }
}