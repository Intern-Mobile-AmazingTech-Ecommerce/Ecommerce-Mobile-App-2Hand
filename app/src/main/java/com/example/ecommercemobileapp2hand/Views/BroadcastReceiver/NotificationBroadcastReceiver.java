package com.example.ecommercemobileapp2hand.Views.BroadcastReceiver;



import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ecommercemobileapp2hand.Controllers.NotificationsHandler;
import com.example.ecommercemobileapp2hand.Models.Notifications;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("GLOBAL_NOTIFICATION")) {
                // Extract data from the Intent
                String title = intent.getStringExtra("title");
                String body = intent.getStringExtra("body");
                // Log the received data for debugging
                Log.d(TAG, "Received notification with title: " + title);
                Log.d(TAG, "Received notification with body: " + body);


                // Create the notification content
                String notificationContent = "Title: " + title + "\nBody: " + body;

                // Create a new notification object
                Notifications notification = new Notifications();
                notification.setNotifications_content(notificationContent);
                notification.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                notification.setUser_id("1"); // Use the user_id from the Intent
                notification.setViewed(false);

                // Save the notification to the database
                NotificationsHandler.saveNotification(notification);

                // Optional: Update the UI or notify other parts of the application
                Log.d(TAG, "Notification saved successfully.");
            }
        }
    }
}

