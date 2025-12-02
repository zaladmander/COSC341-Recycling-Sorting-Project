package com.example.cosc341_recycling_sorting_project.ui.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cosc341_recycling_sorting_project.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "recycleChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Recycling Pickup Reminder")
                .setContentText("Pickup is today! Check your schedule.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(2001, builder.build());
    }
}
