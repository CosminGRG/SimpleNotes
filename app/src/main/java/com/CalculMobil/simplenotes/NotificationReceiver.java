package com.CalculMobil.simplenotes;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationClickIntent = new Intent(context, NoteDetails.class);
        notificationClickIntent.putExtra("title", intent.getStringExtra("noteTitle"));
        notificationClickIntent.putExtra("content", intent.getStringExtra("noteContent"));
        notificationClickIntent.putExtra("noteId", intent.getStringExtra("noteId"));

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationClickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notif");
        builder.setSmallIcon(R.drawable.ic_baseline_add_box_24)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("text"))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = intent.getIntExtra("notificationId", 0);

        notificationManager.notify(notificationId, builder.build());
    }
}
