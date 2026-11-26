
package com.example.uidesignlab;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    private volatile boolean running = true;

    @Override
    public void onCreate() {
        super.onCreate();

        running = true;

        Log.d(TAG, "Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "Service Started. Start ID: " + startId);

        running = true;

        // Get file name from Intent
        String fileName = "unknown_file";

        if (intent != null) {

            String receivedFileName =
                    intent.getStringExtra("FILE_NAME");

            if (receivedFileName != null &&
                    !receivedFileName.trim().isEmpty()) {

                fileName = receivedFileName;
            }
        }

        final String taskFileName = fileName;

        Log.d(TAG, "File name: " + taskFileName);

        executor.execute(() -> {

            for (int i = 1; i <= 5; i++) {

                if (!running ||
                        Thread.currentThread().isInterrupted()) {

                    Log.d(TAG, "Download Stopped");
                    return;
                }

                try {

                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    Log.d(TAG, "Download Interrupted");

                    return;
                }

                if (!running ||
                        Thread.currentThread().isInterrupted()) {

                    Log.d(TAG, "Download Stopped");
                    return;
                }

                Log.d(TAG, "Downloading "
                        + taskFileName + "... " + (i * 20) + "%");
            }

            Log.d(TAG, "Download Completed: " + taskFileName);

            stopSelf(startId);
        });

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        running = false;

        executor.shutdownNow();

        Log.d(TAG, "Service Destroyed");

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
