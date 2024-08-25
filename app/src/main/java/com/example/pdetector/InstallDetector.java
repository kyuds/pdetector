package com.example.pdetector;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class InstallDetector extends BroadcastReceiver {
    public static final String PACKAGE_NAME = "packageName";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction()) && intent.getData() != null) {
            String pkg = intent.getData().getSchemeSpecificPart();

            Log.d("INSTALLED PACKAGE", pkg);

            Notification updatedNotification = new NotificationCompat.Builder(context, DetectionService.DETECTION_CHANNEL)
                    .setContentTitle("Installed")
                    .setContentText(pkg)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(1, updatedNotification); // Same ID used in startForeground
            }
        }
    }
}
