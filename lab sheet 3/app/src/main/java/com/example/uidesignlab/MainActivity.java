package com.example.uidesignlab;

import android.content.Intent;
import android.widget.EditText;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleTracker";
    private static final int COLOR_NORMAL = Color.parseColor("#6200EE");
    private static final int COLOR_PRESSED = Color.parseColor("#4CAF50");

    private ToneGenerator toneGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        Log.d(TAG, "onCreate: Activity is being created");

        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        EditText editUsername = findViewById(R.id.editUsername);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setBackgroundColor(COLOR_NORMAL);

        btnSave.setOnClickListener(v -> {

            String username = editUsername.getText().toString();
            Toast.makeText(this, "Hello, " + username, Toast.LENGTH_SHORT).show();
            btnSave.setBackgroundColor(COLOR_PRESSED);
            new Handler(Looper.getMainLooper()).postDelayed(
                    () -> btnSave.setBackgroundColor(COLOR_NORMAL), 3000);
            toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 400);
            TextView result = new TextView(this);
            result.setId(View.generateViewId());
            result.setText("Profile Saved Successfully!");
            result.setTextColor(Color.parseColor("#2E7D32"));
            result.setTextSize(18);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            params.topToBottom = R.id.editEmail;
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            params.topMargin = 48;

            mainLayout.addView(result, params);

            String userName = editUsername.getText().toString().trim();

            if (userName.isEmpty()) {
                userName = "Student";
            }

            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);

            intent.putExtra("USER_NAME", userName);

            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Activity is becoming visible");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity is in the foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Activity is losing focus");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Activity is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Activity is being destroyed");
        if (toneGenerator != null) {
            toneGenerator.release();
        }
    }
}