package com.example.uidesignlab;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;

public class BackgroundActivity extends AppCompatActivity {

    private static final String TAG = "BackgroundLab";

    private TextView txtStatus;
    private ProgressBar progressBar;
    private Button btnStartHeavy;
    private Button btnFreezeUi;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    private final Handler mainHandler =
            new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        txtStatus = findViewById(R.id.txtStatus);
        progressBar = findViewById(R.id.progressBar);

        btnStartHeavy = findViewById(R.id.btnStartHeavy);
        btnFreezeUi = findViewById(R.id.btnFreezeUi);

        btnFreezeUi.setOnClickListener(v -> freezeTheUi());

        btnStartHeavy.setOnClickListener(v -> startHeavyTask());

        Button btnStartService = findViewById(R.id.btnStartService);

        btnStartService.setOnClickListener(v -> {
            Intent intent = new Intent(
                    BackgroundActivity.this,
                    DownloadService.class
            );
            startService(intent);
        });
    }

    private void freezeTheUi() {

        Log.d(TAG, "Freeze UI Thread: "
                + Thread.currentThread().getName());

        txtStatus.setText("UI is freezing... Please wait!");

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        txtStatus.setText("Done (the screen was frozen)");

        Log.d(TAG, "Freeze UI completed");
    }

    private void startHeavyTask() {

        btnStartHeavy.setEnabled(false);

        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);

        txtStatus.setText("Working...");

        executor.execute(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, "Heavy Task Thread: "
                        + Thread.currentThread().getName());

                for (int i = 1; i <= 5; i++) {

                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {

                        Thread.currentThread().interrupt();
                        return;
                    }

                    final int step = i;

                    mainHandler.post(new Runnable() {

                        @Override
                        public void run() {

                            progressBar.setProgress(step);

                            txtStatus.setText(
                                    "Progress: " + step + "/5"
                            );
                        }
                    });
                }

                mainHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        txtStatus.setText(
                                "Task Completed Successfully!"
                        );

                        progressBar.setVisibility(View.GONE);

                        btnStartHeavy.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
