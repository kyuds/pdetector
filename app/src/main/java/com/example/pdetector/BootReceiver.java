package com.example.pdetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BOOT RECEIVER", "received boot msg");
        if (intent.getAction() != null &&
                intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) &&
                !DetectionService.isRunning(context))
        {
            Intent serviceIntent = new Intent(context, DetectionService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            }
        }
    }
}
