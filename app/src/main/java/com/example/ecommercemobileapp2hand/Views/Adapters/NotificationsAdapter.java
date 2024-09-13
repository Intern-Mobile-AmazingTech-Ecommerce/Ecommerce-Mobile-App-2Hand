package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.Notifications;
import com.example.ecommercemobileapp2hand.R;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<Notifications> notificationsList;

    public NotificationsAdapter(List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifications notification = notificationsList.get(position);
        holder.textViewNotificationContent.setText(notification.getNotifications_content());
        if (notification.isViewed()) {
            // Nếu thông báo đã được đọc, hiển thị hình ảnh cho thông báo đã đọc
            holder.imageViewNotification.setImageResource(R.drawable.notificationbing);
        } else {
            // Nếu thông báo chưa được đọc, hiển thị hình ảnh mặc định
            holder.imageViewNotification.setImageResource(R.drawable.notificationbing_red);
        }
    }

    @Override
    public int getItemCount() {
        return notificationsList != null ? notificationsList.size() : 0;
    }

    public void setNotificationsList(List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewNotification;
        TextView textViewNotificationContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewNotification = itemView.findViewById(R.id.imageViewNotification);
            textViewNotificationContent = itemView.findViewById(R.id.textViewNotificationContent);
        }
    }
}
