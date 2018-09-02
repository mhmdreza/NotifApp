package com.example.mhmdreza_j.notifapp.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mhmdreza_j.notifapp.R;

import static com.example.mhmdreza_j.notifapp.ui.MainActivity.CHANNEL_ID;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.PROGRESS_BAR_MAX_VALUE;


public class ProgressNotificationService extends IntentService {
    public static final int PROGRESS_NOTIFICATION_ID = 3;

    public ProgressNotificationService() {
        super("ProgressNotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int max = intent.getIntExtra(PROGRESS_BAR_MAX_VALUE, 0);
        showNotification(max);
    }

    private void showNotification(int max) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.small_icon)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        for (int i = 1; i <= max; i++) {
            builder.setProgress(max, i, false);
            notificationManager.notify(PROGRESS_NOTIFICATION_ID, builder.build());
        }
        notificationManager.cancel(PROGRESS_NOTIFICATION_ID);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Picture Download")
                .setContentText("Download completed")
                .setSmallIcon(R.drawable.small_icon)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(PROGRESS_NOTIFICATION_ID, builder.build());
    }

}
