package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleTracker";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.d(TAG, "Dashboard onCreate");
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        setTitle(R.string.dashboard_title);

        String userName = getIntent().getStringExtra("USER_NAME");
        TextView txtUserName = findViewById(R.id.txtUserName);
        txtUserName.setText(getString(R.string.hello_user, userName));

        // Find RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

// Set vertical layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

// Create data list
        List<String> items = new ArrayList<>();

        items.add("Activity");
        items.add("Service");
        items.add("Broadcast Receiver");
        items.add("Content Provider");
        items.add("Intent");
        items.add("Fragment");
        items.add("View");
        items.add("Layout");
        items.add("Manifest");
        items.add("Adapter");
        items.add("RecyclerView");
        items.add("Menu");

// Create Adapter
        MyAdapter myAdapter = new MyAdapter(items);

// Connect Adapter to RecyclerView
        recyclerView.setAdapter(myAdapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashboard_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Toast.makeText(this,
                    "Settings clicked",
                    Toast.LENGTH_SHORT).show();

            return true;

        } else if (id == R.id.action_logout) {

            new AlertDialog.Builder(this)

                    .setTitle("Logout")

                    .setMessage("Are you sure you want to log out?")

                    .setPositiveButton("Yes", (dialog, which) -> finish())

                    .setNegativeButton("No", null)

                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
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