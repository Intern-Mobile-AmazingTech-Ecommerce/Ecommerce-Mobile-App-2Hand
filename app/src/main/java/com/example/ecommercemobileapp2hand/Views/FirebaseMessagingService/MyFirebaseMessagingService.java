package com.example.ecommercemobileapp2hand.Views.FirebaseMessagingService;

import com.example.ecommercemobileapp2hand.Controllers.UserOrderHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
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
//        Log.d("MyFirebaseMessagingService", "Tin nhắn đã nhận: " + remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0) {
            handleDataMessage(remoteMessage);
        }
//        else if (remoteMessage.getNotification() != null) {
//
//            String title = remoteMessage.getNotification().getTitle();
//            String body = remoteMessage.getNotification().getBody();
//            handleSystemEventMessage(title, body);
//
//
//        }

    }
    private void handleDataMessage(RemoteMessage remoteMessage) {
        String userOrderId = remoteMessage.getData().get("user_order_id");
        String orderStatusId = remoteMessage.getData().get("order_status_id");

        // Kiểm tra nếu userId từ tin nhắn khớp với userId của người dùng hiện tại
//        String currentUserId = getCurrentUserId();
//        if (!Objects.equals(userId, currentUserId)) {
//            // Nếu không phải userId của người dùng hiện tại, không làm gì cả
//            Log.d("MyFirebaseMessagingService", "UserId không khớp, không gửi thông báo.");
//            return;
//        }

        // Chuyển đổi order_status_id sang order_status_name
        String orderStatusName = getOrderStatusName(orderStatusId);
        String notificationContent = "Order #" + userOrderId + " changed status to " + orderStatusName;

        UserOrderHandler.updateOrderStatus(Integer.parseInt(userOrderId), Integer.parseInt(orderStatusId), new UserOrderHandler.Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                if (result) {
//                    Log.d("MyFirebaseMessagingService", "Đã cập nhật trạng thái đơn hàng thành công.");
                } else {
//                    Log.d("MyFirebaseMessagingService", "Cập nhật trạng thái đơn hàng thất bại.");
                }
            }
        });
        UserOrderHandler.getUserOrderById(Integer.parseInt(userOrderId), new UserOrderHandler.Callback<UserOrder>() {
            @Override
            public void onResult(UserOrder userOrder) {
                if (userOrder != null) {
                    String userId = userOrder.getUser_id();

                    // Tạo đối tượng thông báo mới
                    Notifications notification = new Notifications();
                    notification.setNotifications_content(notificationContent);
                    notification.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                    notification.setUser_id(userId);  // Lưu user_id từ đơn hàng
                    notification.setViewed(false);
                    NotificationsHandler.saveNotification(notification);
                    // Gửi thông báo đến user đó (nếu cần)
                    // sendOrderStatusNotification("Cập nhật trạng thái đơn hàng", notificationContent);
                } else {
//                    Log.d("MyFirebaseMessagingService", "Không tìm thấy đơn hàng với userOrderId: " + userOrderId);
                }
            }
        });
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


//    private void handleSystemEventMessage(String title, String body) {
//        service.execute(() -> {
//            String adminUserId = "1";  // Giả sử admin có userId = 1, hoặc bạn có thể lấy từ cơ sở dữ liệu
//
//            // Kiểm tra nếu userId khớp với admin (hoặc người dùng cần nhận thông báo)
//            String currentUserId = getCurrentUserId();
//            if (!Objects.equals(adminUserId, currentUserId)) {
//                Log.d("MyFirebaseMessagingService", "Không phải admin, không gửi thông báo sự kiện.");
//
//                return;
//            }
//
//            // Tạo đối tượng thông báo mới
//            Notifications notification = new Notifications();
//            notification.setNotifications_content(body);
//            notification.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
//            notification.setUser_id(adminUserId);  // Admin userId
//            notification.setViewed(false);
//
//            // Lưu thông báo vào cơ sở dữ liệu
//            NotificationsHandler.saveNotification(notification);
//
//            // Gửi thông báo cho admin
//            sendNotification(title, body);
//        });
//    }

//    private String getCurrentUserId() {
//
//        return UserAccountManager.getInstance().getCurrentUserAccount().getUserId();
//    }

//    private void sendNotification(String messageTitle, String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("navigateTo", "NotificationsDetailFragment");
//
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//
//        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notificationsmall_layout);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notificationlarge_layout);
//        notificationLayout.setTextViewText(R.id.notification_title, messageTitle);
//        notificationLayoutExpanded.setTextViewText(R.id.notification_title, messageTitle);
//        notificationLayoutExpanded.setTextViewText(R.id.notification_body, messageBody);
//
//        String channelId = "default_channel";
//        Notification customNotification = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.notificationbing)
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                .setCustomContentView(notificationLayout)
//                .setCustomBigContentView(notificationLayoutExpanded)
////                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, "Thông báo", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(666, customNotification);
//    }

//    private void sendOrderStatusNotification(String messageTitle, String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("navigateTo", "NotificationsDetailFragment");
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//
//        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notificationsmall_layout);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notificationlarge_layout);
//        notificationLayout.setTextViewText(R.id.notification_title, messageTitle);
//        notificationLayoutExpanded.setTextViewText(R.id.notification_title, messageTitle);
//        notificationLayoutExpanded.setTextViewText(R.id.notification_body, messageBody);
//
//        String channelId = "order_update_channel";
//        Notification customNotification = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.notificationbing)
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                .setCustomContentView(notificationLayout)
//                .setCustomBigContentView(notificationLayoutExpanded)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, "Order Updates", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(666, customNotification);
//    }


}