package com.example.mhmdreza_j.notifapp.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mhmdreza_j.notifapp.R;

import static com.example.mhmdreza_j.notifapp.ui.MainActivity.BIG_TEXT_NOTIFICATION_BIG_TEXT;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.BIG_TEXT_NOTIFICATION_TEXT;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.BIG_TEXT_NOTIFICATION_TITLE;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.CHANNEL_ID;


public class BigTextNotificationService extends IntentService {
    private static final int BIG_TEXT_NOTIFICATION = 5;

    public BigTextNotificationService() {
        super("BigTextNotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String text = intent.getStringExtra(BIG_TEXT_NOTIFICATION_TEXT);
            String title = intent.getStringExtra(BIG_TEXT_NOTIFICATION_TITLE);
            String bigText = intent.getStringExtra(BIG_TEXT_NOTIFICATION_BIG_TEXT);
            showNotification(text, title, bigText);
        }
    }

    private void showNotification(String text, String title, String bigText) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentText(text)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(BIG_TEXT_NOTIFICATION, notification);
    }
}
