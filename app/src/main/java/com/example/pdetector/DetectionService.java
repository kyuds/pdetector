package com.example.pdetector;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class DetectionService extends Service {

    public static final String DETECTION_CHANNEL = "detection_channel";
    public static final int SERVICE_ID = 1001;

    private InstallDetector detector;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    DETECTION_CHANNEL, DETECTION_CHANNEL, NotificationManager.IMPORTANCE_LOW
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, DETECTION_CHANNEL)
                    .setContentTitle("pdetector")
                    .setContentText("Detector Is Running")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setOngoing(true)
                    .build();
            startForeground(SERVICE_ID, notification);
            Log.d("SERVICE", "Instantiated...");
        }

        // register detection receiver.
        detector = new InstallDetector();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        registerReceiver(detector, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE", "Destroying...");
        if (detector != null) {
            unregisterReceiver(detector);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Check whether this service is already instantiated.
    public static boolean isRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(DetectionService.class.getName())) {
                return true;
            }
        }
        return false;
    }
}
