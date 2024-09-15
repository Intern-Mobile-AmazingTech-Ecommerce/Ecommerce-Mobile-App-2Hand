package com.example.ecommercemobileapp2hand.Views.FirebaseMessagingService;

import com.example.ecommercemobileapp2hand.Controllers.UserOrderHandler;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.example.ecommercemobileapp2hand.Views.Notifications.NotificationDetailFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.ecommercemobileapp2hand.Controllers.NotificationsHandler;
import com.example.ecommercemobileapp2hand.Models.Notifications;

import java.util.List;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    ExecutorService service = Executors.newCachedThreadPool();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Kiểm tra nếu tin nhắn có data

//        Log.d("MyFirebaseMessagingService", "Tin nhắn đã nhận: " + remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0) {
            handleDataMessage(remoteMessage);
        }


    }
    private void handleDataMessage(RemoteMessage remoteMessage) {
        String userOrderId = remoteMessage.getData().get("user_order_id");
        String orderStatusId = remoteMessage.getData().get("order_status_id");
        String userId = remoteMessage.getData().get("user_id");
        // Chuyển đổi order_status_id sang order_status_name
        String orderStatusName = getOrderStatusName(orderStatusId);
        // Tạo nội dung thông báo
        String notificationContent = "Order #" + userOrderId + " changed status to " + orderStatusName;

        UserOrderHandler.updateOrderStatus(Integer.parseInt(userOrderId), Integer.parseInt(orderStatusId), new UserOrderHandler.Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {

            }
        });
        // Tạo đối tượng thông báo mới
        Notifications notification = new Notifications();
        notification.setNotifications_content(notificationContent);
        notification.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        notification.setUser_id(userId);
        notification.setViewed(false);

        // Lưu thông báo vào cơ sở dữ liệu
        NotificationsHandler.saveNotification(notification);
        sendOrderStatusNotification("Cập nhật trạng thái đơn hàng", notificationContent);
    }

    private String getOrderStatusName(String orderStatusId) {
        switch (orderStatusId) {
            case "1":
                return "Processing";
            case "2":
                return "Order Placed";
            case "3":
                return "Order Confirmed";
            case "4":
                return "Shipped";
            case "5":
                return "Delivered";
            case "6":
                return "Returned";
            case "7":
                return "Canceled";
            default:
                return "Unknown Status";
        }
    }



    private void sendOrderStatusNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("navigateTo", "NotificationsDetailFragment");  // No order ID passed

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Create RemoteViews for custom notification layouts
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notificationsmall_layout);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notificationlarge_layout);

        // Set notification title and body
        notificationLayout.setTextViewText(R.id.notification_title, messageTitle);
        notificationLayoutExpanded.setTextViewText(R.id.notification_title, messageTitle);
        notificationLayoutExpanded.setTextViewText(R.id.notification_body, messageBody);

        String channelId = "order_update_channel";

        // Build custom notification
        Notification customNotification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notificationbing)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Order Updates", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Show the notification
        notificationManager.notify(666, customNotification);
    }

}
