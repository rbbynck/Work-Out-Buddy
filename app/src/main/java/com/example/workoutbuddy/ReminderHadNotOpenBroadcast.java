package com.example.workoutbuddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderHadNotOpenBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, IntroActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 777, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyWorkoutBuddy")
                .setSmallIcon(R.drawable.ic_icon)
                .setContentTitle("Workout Buddy")
                .setContentText("You haven't opened the app for three days. What happened?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pi);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(777, builder.build());
    }

}