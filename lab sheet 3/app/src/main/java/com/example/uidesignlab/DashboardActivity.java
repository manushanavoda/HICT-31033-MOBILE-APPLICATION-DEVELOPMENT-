package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleTracker";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.d(TAG, "Dashboard onCreate");

        String userName = getIntent().getStringExtra("USER_NAME");
        TextView txtUserName = findViewById(R.id.txtUserName);
        txtUserName.setText(getString(R.string.hello_user, userName));
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Dashboard onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Dashboard onDestroy");
    }
}