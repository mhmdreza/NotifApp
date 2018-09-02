package com.example.mhmdreza_j.notifapp.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.mhmdreza_j.notifapp.R;
import com.example.mhmdreza_j.notifapp.ui.MainActivity;

import static com.example.mhmdreza_j.notifapp.ui.MainActivity.CHANNEL_ID;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.GROUP_NOTIFICATION_ID;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.SIMPLE_NOTIFICATION_TEXT;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.SIMPLE_NOTIFICATION_TITLE;

public class GroupNotificationService extends SimpleNotificationService {
    private static final String GROUP_KEY = "group key";
    private int notificationID;

    public GroupNotificationService() {
        super("GroupNotificationService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String text = intent.getStringExtra(SIMPLE_NOTIFICATION_TEXT);
            notificationID = intent.getIntExtra(GROUP_NOTIFICATION_ID, 0);
            String title = intent.getStringExtra(SIMPLE_NOTIFICATION_TITLE) + String.valueOf(notificationID) + " asdfgh";
            showNotification(text, title);
        }
    }

    @Override
    void showNotification(String text, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder notificationCompat = createBuilderForNotification(text, title, pendingIntent, CHANNEL_ID);
        DisplayNotification(notificationID, notificationCompat);
    }

    @Override
    protected NotificationCompat.Builder createBuilderForNotification(String text, String title, PendingIntent pendingIntent, String channelId) {
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return notificationCompat;
    }
}
