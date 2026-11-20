
package com.example.uidesignlab;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends Service {

    private static final String TAG = "BackgroundLab";

    private final Handler mainHandler =
            new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DownloadService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "DownloadService onStartCommand, startId = " + startId);

        mainHandler.post(() ->
                Toast.makeText(this, "Download started",
                        Toast.LENGTH_SHORT).show()
        );

        new Thread(() -> {

            for (int i = 1; i <= 5; i++) {

                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                Log.d(TAG, "Download progress: " + (i * 20) + "%");
            }

            mainHandler.post(() ->
                    Toast.makeText(this, "Download finished",
                            Toast.LENGTH_SHORT).show()
            );

            stopSelf(startId);

        }).start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DownloadService onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
