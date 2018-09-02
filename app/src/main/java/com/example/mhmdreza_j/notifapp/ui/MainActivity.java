package com.example.mhmdreza_j.notifapp.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.mhmdreza_j.notifapp.R;
import com.example.mhmdreza_j.notifapp.services.BigTextNotificationService;
import com.example.mhmdreza_j.notifapp.services.GroupNotificationService;
import com.example.mhmdreza_j.notifapp.services.NotificationWithReplyService;
import com.example.mhmdreza_j.notifapp.services.ProgressNotificationService;
import com.example.mhmdreza_j.notifapp.services.ScreenshotNotificationService;
import com.example.mhmdreza_j.notifapp.services.SimpleNotificationService;

import java.io.ByteArrayOutputStream;

import github.nisrulz.screenshott.ScreenShott;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "channel";
    public static final CharSequence CHANNEL_NAME = "name";
    public static final String CHANNEL_DESCRIPTION = "desc";
    public static final String REPLY_NOTIFICATION_TEXT = "reply notification text";
    public static final String REPLY_NOTIFICATION_TITLE = "reply notification title";
    public static final String SIMPLE_NOTIFICATION_TEXT = "simple notification text";
    public static final String SIMPLE_NOTIFICATION_TITLE = "simple notification title";
    public static final String PROGRESS_BAR_MAX_VALUE = "progress bar max value";
    private static final int DEFAULT_MAX_VALUE = 300;
    public static final String SCREENSHOT = "screenshot";
    public static final String BIG_TEXT_NOTIFICATION_TEXT = "big text notification text";
    public static final String BIG_TEXT_NOTIFICATION_TITLE = "big text notification title";
    public static final String BIG_TEXT_NOTIFICATION_BIG_TEXT = "big text notification big text";
    public static final String GROUP_NOTIFICATION_ID = "group notification id";
    private int gorupNotificationId;

    EditText notificationTitleEditText;
    EditText notificationTextEditText;
    EditText progressBarMaxEditText;
    EditText bigTextNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        initializeViews();
        gorupNotificationId = -1;
    }

    private void initializeViews() {
        notificationTextEditText = findViewById(R.id.edittext_notification_text);
        notificationTitleEditText = findViewById(R.id.edittext_notification_title);
        progressBarMaxEditText = findViewById(R.id.edittext_max_progress_value);
        bigTextNotification = findViewById(R.id.edittext_big_text);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void createSimpleNotification(View view) {
        Intent intent = new Intent(MainActivity.this, SimpleNotificationService.class);
        String text = notificationTextEditText.getText().toString();
        String title = notificationTitleEditText.getText().toString();
        intent.putExtra(SIMPLE_NOTIFICATION_TEXT, text);
        intent.putExtra(SIMPLE_NOTIFICATION_TITLE, title);
        startService(intent);
    }

    public void createNotificationWithReply(View view) {
       Intent serviceIntent = new Intent(MainActivity.this, NotificationWithReplyService.class);
        String text = notificationTextEditText.getText().toString();
        String title = notificationTitleEditText.getText().toString();
        serviceIntent.putExtra(REPLY_NOTIFICATION_TEXT, text);
        serviceIntent.putExtra(REPLY_NOTIFICATION_TITLE, title);
        startService(serviceIntent);
    }

    public void createProgressBarNotification(View view) {
        int progressBarMaxValue;
        try {
            progressBarMaxValue = Integer.parseInt(progressBarMaxEditText.getText().toString());
        }catch (Exception e){
            progressBarMaxValue = DEFAULT_MAX_VALUE;
        }
        Intent intent = new Intent(MainActivity.this, ProgressNotificationService.class);
        intent.putExtra(PROGRESS_BAR_MAX_VALUE, progressBarMaxValue);
        startService(intent);
    }

    public void createScreenshotNotification(View view) {
        Bitmap screenshot = ScreenShott.getInstance().takeScreenShotOfRootView(view);
        Intent intent = new Intent(MainActivity.this, ScreenshotNotificationService.class);
        intent.putExtra(SCREENSHOT, convertBitmapToByteArray(screenshot));
        startService(intent);
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        return bStream.toByteArray();
    }

    public void createLargeTextNotification(View view) {
        String text = notificationTextEditText.getText().toString();
        String title = notificationTitleEditText.getText().toString();
        String bigText  = bigTextNotification.getText().toString();
        Intent intent = new Intent(MainActivity.this, BigTextNotificationService.class);
        intent.putExtra(BIG_TEXT_NOTIFICATION_TEXT, text);
        intent.putExtra(BIG_TEXT_NOTIFICATION_TITLE, title);
        intent.putExtra(BIG_TEXT_NOTIFICATION_BIG_TEXT, bigText);
        startService(intent);
    }

    public void addNotificationToGroup(View view) {
        int SUMMARY_ID = 0;
        Notification summaryNotification =
                new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)

                        //set content text to support devices running API level < 24
                        .setSmallIcon(R.drawable.small_icon)
                        //build summary info into InboxStyle template
                        //specify which group this notification belongs to
                        .setGroup("group key")
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(SUMMARY_ID, summaryNotification);
        Intent intent = new Intent(MainActivity.this, GroupNotificationService.class);
        String text = notificationTextEditText.getText().toString();
        String title = notificationTitleEditText.getText().toString();
        intent.putExtra(SIMPLE_NOTIFICATION_TEXT, text);
        intent.putExtra(SIMPLE_NOTIFICATION_TITLE, title);
        intent.putExtra(GROUP_NOTIFICATION_ID, gorupNotificationId);
        gorupNotificationId--;
        startService(intent);
    }
}
