package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.R;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderAdapter.MyViewHolder> {
    ArrayList<OrderStatus> lst_ordstt;
    Context context;
    UserOrder order;

    public TrackOrderAdapter(ArrayList<OrderStatus> lst_ordstt, Context context, UserOrder order) {
        this.lst_ordstt = lst_ordstt;
        this.context = context;
        this.order = order;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewholder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_trackodercard, parent, false);
        return new TrackOrderAdapter.MyViewHolder(viewholder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderStatus orderStatus = lst_ordstt.get(position);

        if (orderStatus.getOrder_status_id()  <= order.getOrder_status_id())
        {
            holder.img_checkstattus.setImageResource(R.drawable.check_line);
            holder.img_checkstattus.setBackgroundResource(R.drawable.circle_completed);
            holder.tv_orderstatus.setText(orderStatus.getOrder_status_name());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM");
            String formattedDate = order.getCreated_at().format(formatter);
            holder.tv_created.setText(formattedDate);
        }
        else
        {
            int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            boolean isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
            if (isDarkMode)
            {
                holder.tv_orderstatus.setTextColor(Color.parseColor("#8E8C94"));
                holder.tv_created.setTextColor(Color.parseColor("#8E8C94"));
            }
            else
            {
                holder.tv_orderstatus.setTextColor(Color.parseColor("#939393"));
                holder.tv_created.setTextColor(Color.parseColor("#939393"));
            }
            holder.img_checkstattus.setImageResource(R.drawable.check_line);
            holder.img_checkstattus.setBackgroundResource(R.drawable.circle_incompleted);
            holder.tv_orderstatus.setText(orderStatus.getOrder_status_name());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM");
            String formattedDate = order.getCreated_at().format(formatter);
            holder.tv_created.setText(formattedDate);
        }
    }

    @Override
    public int getItemCount() {
        return lst_ordstt.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView img_checkstattus;
        private TextView tv_orderstatus, tv_created;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img_checkstattus = itemView.findViewById(R.id.img_checkstatus);
            tv_orderstatus = itemView.findViewById(R.id.tv_orderstatus);
            tv_created = itemView.findViewById(R.id.tv_created);
        }
    }
}
