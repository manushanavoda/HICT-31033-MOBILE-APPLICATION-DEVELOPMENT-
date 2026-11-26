
package com.example.uidesignlab;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

            intent.putExtra("FILE_NAME", "maternal_health_report.pdf");

            startService(intent);
        });



        Button btnStopService = findViewById(R.id.btnStopService);

        btnStopService.setOnClickListener(v -> {

            Intent intent = new Intent(
                    BackgroundActivity.this,
                    DownloadService.class
            );

            stopService(intent);
        });




        Button btnStartLeaky = findViewById(R.id.btnStartLeaky);
        btnStartLeaky.setOnClickListener(v -> startLeakyTask());

        Button btnStartSafe = findViewById(R.id.btnStartSafe);
        btnStartSafe.setOnClickListener(v -> startSafeTask());
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

    // TASK 1: Lambda Expressions

    private void startHeavyTask() {

        btnStartHeavy.setEnabled(false);

        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);

        txtStatus.setText("Calculating prime numbers...");

        executor.execute(() -> {

            Log.d(TAG, "Calculation Thread: "
                    + Thread.currentThread().getName());

            long totalPrimeSum = 0;
            int primeCount = 0;

            int limit = 400000;

            for (int number = 2; number < limit; number++) {

                boolean isPrime = true;

                for (int divisor = 2;
                     divisor * divisor <= number;
                     divisor++) {

                    if (number % divisor == 0) {
                        isPrime = false;
                        break;
                    }
                }

                if (isPrime) {
                    totalPrimeSum += number;
                    primeCount++;
                }

                if (number % 10000 == 0) {

                    final int progress =
                            (number * 100) / limit;

                    mainHandler.post(() -> {

                        progressBar.setProgress(progress);

                        txtStatus.setText(
                                "Calculating: " + progress + "%"
                        );
                    });
                }
            }

            final long finalSum = totalPrimeSum;
            final int finalCount = primeCount;

            Log.d(TAG, "Prime Calculation Completed");
            Log.d(TAG, "Prime Count: " + finalCount);
            Log.d(TAG, "Prime Sum: " + finalSum);

            mainHandler.post(() -> {

                txtStatus.setText(
                        "Calculation Completed!\n"
                                + "Prime Count: " + finalCount
                                + "\nPrime Sum: " + finalSum
                );

                progressBar.setProgress(100);
                progressBar.setVisibility(View.GONE);

                btnStartHeavy.setEnabled(true);
            });
        });
    }


    // LEAKY TASK
    private void startLeakyTask() {

        txtStatus.setText("Leaky task started");

        executor.execute(() -> {

            for (int i = 1; i <= 120; i++) {

                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    Log.d(TAG, "Leaky task was interrupted: stop");
                    Thread.currentThread().interrupt();
                    return;
                }

                if (i % 10 == 0) {

                    Log.d(TAG, "Leaky task alive, activity id = "
                            + System.identityHashCode(
                            BackgroundActivity.this
                    ));
                }
            }
        });
    }

    // SAFE TASK
    private void startSafeTask() {

        txtStatus.setText("Safe task started");

        SafeTask safeTask = new SafeTask(this);

        executor.execute(safeTask);
    }

    // STATIC CLASS WITH WEAKREFERENCE
    private static class SafeTask implements Runnable {

        private final WeakReference<BackgroundActivity> activityRef;

        SafeTask(BackgroundActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        public void run() {

            for (int i = 1; i <= 120; i++) {

                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                BackgroundActivity activity = activityRef.get();

                if (activity == null || activity.isDestroyed()) {
                    Log.d(TAG, "Safe task: the screen is gone, stop work");
                    return;
                }

                if (i % 10 == 0) {
                    Log.d(TAG, "Safe task alive, activity id = "
                            + System.identityHashCode(activity));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {

        Log.d(TAG, "BackgroundActivity onDestroy");

        executor.shutdownNow();

        mainHandler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
