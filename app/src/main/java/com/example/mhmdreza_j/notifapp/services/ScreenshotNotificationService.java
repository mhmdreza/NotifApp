package com.example.mhmdreza_j.notifapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.mhmdreza_j.notifapp.R;

import static com.example.mhmdreza_j.notifapp.ui.MainActivity.CHANNEL_ID;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.SCREENSHOT;

public class ScreenshotNotificationService extends IntentService {

    private static final int SCREENSHOT_NOTIFICATION_ID = 4;

    public ScreenshotNotificationService() {
        super("ScreenshotNotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    Handler handler;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Bitmap screenshot;
            byte[] byteArray = intent.getByteArrayExtra(SCREENSHOT);
            screenshot = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            showNotification(screenshot);
        }
    }

    private void showNotification(Bitmap screenshot) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_camera)
                .setLargeIcon(screenshot)
                .setContentTitle("Screenshot captured")
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(screenshot)
                        .bigLargeIcon(null))
                .setContentText("tap here to view it");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(SCREENSHOT_NOTIFICATION_ID, builder.build());
    }
}
