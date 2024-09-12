//package com.example.ecommercemobileapp2hand.Views.FirebaseMessagingService;
//
//import android.content.Intent;
//import android.util.Log;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//public class MyFirebaseMessagingEvents extends FirebaseMessagingService {
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (remoteMessage.getNotification() != null) {
//            Log.d("FirebaseMessage", "Received message: " + remoteMessage.getData().toString());
//            String title = remoteMessage.getNotification().getTitle();
//            String body = remoteMessage.getNotification().getBody();
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("GLOBAL_NOTIFICATION");
//            broadcastIntent.putExtra("title", title);
//            broadcastIntent.putExtra("body", body);
//            sendBroadcast(broadcastIntent);
//        }
//    }
//
//    @Override
//    public void onNewToken(String token) {
//        Log.d("MyFirebaseMessagingEvents", "Refreshed token: " + token);
//
//        // You can handle the refreshed token here, e.g., send it to your server
//    }
//}
