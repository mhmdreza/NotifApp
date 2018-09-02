package com.example.mhmdreza_j.notifapp.broadcast_reciever;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.mhmdreza_j.notifapp.services.NotificationWithReplyService;
import com.example.mhmdreza_j.notifapp.R;

import static com.example.mhmdreza_j.notifapp.services.NotificationWithReplyService.REPLY_ACTION;

public class NotificationWithReplyReceiver extends BroadcastReceiver {
    private static String KEY_NOTIFICATION_ID = "key_noticiation_id";
    private static String KEY_MESSAGE_ID = "key_message_id";
    private static final String CHANNEL_ID = "channel";

    public NotificationWithReplyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REPLY_ACTION.equals(intent.getAction())) {
            // do whatever you want with the message. Send to the server or add to the db.
            // for this tutorial, we'll just show it in a toast;
            CharSequence message = NotificationWithReplyService.getReplyMessage(intent);

            Toast.makeText(context, "Message: " + message,
                    Toast.LENGTH_SHORT).show();
            // update notification
            int notifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 1);
            updateNotification(context, notifyId);
        }
    }

    private void updateNotification(Context context, int notifyId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentText(context.getString(R.string.notif_content_sent))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), 0));

        notificationManager.notify(notifyId, builder.build());
    }

    public static Intent getReplyMessageIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationWithReplyReceiver.class);
        intent.setAction(REPLY_ACTION);
        intent.putExtra(KEY_NOTIFICATION_ID, notificationId);
        return intent;
    }
}
