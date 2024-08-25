package com.example.pdetector;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t = findViewById(R.id.packageName);
        String pkg = getIntent().getStringExtra(InstallDetector.PACKAGE_NAME);
        if (pkg == null) {
            t.setText("----");
        } else {
            t.setText(pkg);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request the notification permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        } else {
            // Permission already granted, you can proceed with showing notifications
            startDetectorIfNotRunning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with showing notifications
                startDetectorIfNotRunning();
            } else {
                // Permission denied, handle it accordingly (e.g., inform the user)
                Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startDetectorIfNotRunning() {
        if (!DetectionService.isRunning(this)) {
            Intent intent = new Intent(this, DetectionService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }
            // TODO: figure out pre-oreo mechanisms
        }
    }
}
