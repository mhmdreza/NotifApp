package com.example.mhmdreza_j.notifapp.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

import com.example.mhmdreza_j.notifapp.R;
import com.example.mhmdreza_j.notifapp.broadcast_reciever.NotificationWithReplyReceiver;
import com.example.mhmdreza_j.notifapp.ui.MainActivity;

import static com.example.mhmdreza_j.notifapp.ui.MainActivity.CHANNEL_ID;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.REPLY_NOTIFICATION_TEXT;
import static com.example.mhmdreza_j.notifapp.ui.MainActivity.REPLY_NOTIFICATION_TITLE;

public class NotificationWithReplyService extends IntentService {
    private static final int NOTIFICATION_WITH_REPLY_ID = 2;
    public static String REPLY_ACTION = "com.example.mhmdreza_j.notifapp.REPLY_ACTION";
    private static String KEY_REPLY = "key_reply_message";

    Handler handler;

    public NotificationWithReplyService() {
        super("NotificationWithReplyService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String text = intent.getStringExtra(REPLY_NOTIFICATION_TEXT);
            String title = intent.getStringExtra(REPLY_NOTIFICATION_TITLE);
            showNotification(text, title);
        }
    }

    private void showNotification(String text, String title) {
        String replyLabel = getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY)
                .setLabel(replyLabel)
                .build();
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_reply, replyLabel, getReplyPendingIntent())
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentText(text)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(action);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_WITH_REPLY_ID, builder.build());
    }

    private PendingIntent getReplyPendingIntent() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // start a
            // (i)  broadcast receiver which runs on the UI thread or
            // (ii) service for a background task to b executed , but for the purpose of this codelab, will be doing a broadcast receiver
            intent = NotificationWithReplyReceiver.getReplyMessageIntent(this, NOTIFICATION_WITH_REPLY_ID);
            return PendingIntent.getBroadcast(getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return null;
    }

    public static CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_REPLY);
        }
        return null;
    }

}
