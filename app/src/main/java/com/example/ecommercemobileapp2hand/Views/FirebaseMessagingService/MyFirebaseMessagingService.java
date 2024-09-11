package com.example.ecommercemobileapp2hand.Views.FirebaseMessagingService;

import com.example.ecommercemobileapp2hand.Controllers.UserOrderHandler;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Notifications.NotificationDetailFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.ecommercemobileapp2hand.Controllers.NotificationsHandler;
import com.example.ecommercemobileapp2hand.Models.Notifications;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Kiểm tra nếu tin nhắn có data
        Log.d("MyFirebaseMessagingService", "Tin nhắn đã nhận: " + remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0) {
            String userOrderId = remoteMessage.getData().get("user_order_id");
            String orderStatusId = remoteMessage.getData().get("order_status_id");
            String userId = remoteMessage.getData().get("user_id");
            // Chuyển đổi order_status_id sang order_status_name
            String orderStatusName = getOrderStatusName(orderStatusId);
            // Tạo nội dung thông báo
            String notificationContent = "Order #" + userOrderId + " changed status to " + orderStatusName;

            // Update order status in the database
            UserOrderHandler.updateOrderStatus(Integer.parseInt(userOrderId), Integer.parseInt(orderStatusId));
            // Tạo đối tượng thông báo mới
            Notifications notification = new Notifications();
            notification.setNotifications_content(notificationContent);
            notification.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            notification.setUser_id(userId);
            notification.setViewed(false);

            // Lưu thông báo vào cơ sở dữ liệu
            NotificationsHandler.saveNotification(notification);

            // Gửi thông báo đến người dùng
//            sendNotification(userOrderId, notificationContent);
        }
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

//    private void sendNotification(String userOrderId, String messageBody) {
//        Intent intent = new Intent(this, NotificationDetailFragment.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//
//        String channelId = "default_channel";
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.notificationbing)
//                        .setContentTitle("Thông báo đơn hàng")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, "Thông báo", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(Integer.parseInt(userOrderId), notificationBuilder.build());
//    }
    @Override
    public void onNewToken(String token) {
        Log.d("MyFirebaseMessagingService", "FCM Token: " + token);
    }
}
