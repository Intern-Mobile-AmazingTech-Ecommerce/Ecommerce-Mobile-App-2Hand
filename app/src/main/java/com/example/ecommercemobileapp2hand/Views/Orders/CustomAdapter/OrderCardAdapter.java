package com.example.ecommercemobileapp2hand.Views.Orders.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.Order;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Orders.TrackOrderAcitivity;

import java.util.ArrayList;

public class OrderCardAdapter extends RecyclerView.Adapter<OrderCardAdapter.MyViewHoler> {
    ArrayList<Order> lstorders;
    Context context;
    String checkstatus;

    public OrderCardAdapter(ArrayList<Order> lstorders, Context context, String checkstatus) {
        this.lstorders = lstorders;
        this.context = context;
        this.checkstatus = checkstatus;
    }

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewholder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order_card, parent, false);
        return new OrderCardAdapter.MyViewHoler(viewholder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {
        Order order = lstorders.get(position);

        holder.tv_id_order.setText("#" + order.getUser_order_id());
        holder.tv_amount_detail_order.setText(String.valueOf(order.getAmount()) + " items");

        holder.cardview_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTrackOrder(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstorders.size();
    }

    class MyViewHoler extends RecyclerView.ViewHolder
    {
        private CardView cardview_order;
        private TextView tv_id_order, tv_amount_detail_order;
        public MyViewHoler(@NonNull View itemView) {
            super(itemView);
            this.cardview_order = itemView.findViewById(R.id.cardview_order);
            this.tv_id_order = itemView.findViewById(R.id.tv_id_order);
            this.tv_amount_detail_order = itemView.findViewById(R.id.tv_amount_detail_order);
        }
    }

    private void DetailTrackOrder(Order ord)
    {
        Intent intent = new Intent(context, TrackOrderAcitivity.class);
        intent.putExtra("order", ord);
        context.startActivity(intent);
    }
}
