package com.example.ecommercemobileapp2hand.Views.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.ecommercemobileapp2hand.Controllers.NotificationsHandler;
import com.example.ecommercemobileapp2hand.Models.Notifications;

import net.sourceforge.jtds.jdbc.DateTime;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class OrderChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("ORDER_STATUS_CHANGED")) {
                int orderId = intent.getIntExtra("order_id", -1);
                String orderStatus = intent.getStringExtra("order_status");
                String user_Id= intent.getStringExtra("user_id");
                // Tạo nội dung thông báo dựa trên trạng thái đơn hàng
                String notificationContent = "Đơn hàng #" + orderId + " đã thay đổi trạng thái thành " + orderStatus;

                // Tạo đối tượng thông báo mới
                Notifications notification = new Notifications();
                notification.setNotifications_content(notificationContent);
                notification.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                notification.setUser_id(user_Id); // Thay bằng logic lấy ID người dùng hiện tại
                notification.setViewed(false);

                // Lưu thông báo vào cơ sở dữ liệu
                NotificationsHandler.saveNotification(notification);

                // Có thể kích hoạt cập nhật UI


            }
        }
    }

}
