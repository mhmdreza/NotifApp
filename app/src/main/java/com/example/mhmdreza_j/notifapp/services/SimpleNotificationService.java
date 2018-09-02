package com.example.mhmdreza_j.notifapp.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.mhmdreza_j.notifapp.R;
import com.example.mhmdreza_j.notifapp.ui.MainActivity;

import static com.example.mhmdreza_j.notifapp.ui.MainActivity.CHANNEL_ID;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.SIMPLE_NOTIFICATION_TEXT;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.SIMPLE_NOTIFICATION_TITLE;


public class SimpleNotificationService extends IntentService {
    private static final int SIMPLE_NOTIFICATION_ID = 1;

    public SimpleNotificationService() {
        super("SimpleNotificationService");
    }

    public SimpleNotificationService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String text = intent.getStringExtra(SIMPLE_NOTIFICATION_TEXT);
            String title = intent.getStringExtra(SIMPLE_NOTIFICATION_TITLE);
            showNotification(text, title);
        }
    }


     void showNotification(String text, String title) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher_background);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder notificationCompat = createBuilderForNotification(text, title, pendingIntent, CHANNEL_ID);
        notificationCompat.setLargeIcon(drawableToBitmap(drawable));
        DisplayNotification(SIMPLE_NOTIFICATION_ID, notificationCompat);
    }

    protected NotificationCompat.Builder createBuilderForNotification(String text, String title, PendingIntent pendingIntent, String channelId) {
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return notificationCompat;
    }

    protected void DisplayNotification(int notificationId, NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }

    protected static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
