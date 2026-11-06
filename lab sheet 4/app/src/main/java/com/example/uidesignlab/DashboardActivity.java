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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.widget.Button;

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

        int age = getIntent().getIntExtra("USER_AGE", 0);

        Toast.makeText(
                this,
                "Your age is " + age,
                Toast.LENGTH_SHORT
        ).show();

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
        items.add("Button");
        items.add("TextView");
        items.add("EditText");

// Create Adapter
        MyAdapter myAdapter = new MyAdapter(items);

// Connect Adapter to RecyclerView
        recyclerView.setAdapter(myAdapter);

        Button btnWebsite = findViewById(R.id.btnWebsite);

        btnWebsite.setOnClickListener(v -> {

            Intent dialIntent = new Intent(Intent.ACTION_DIAL);

            dialIntent.setData(Uri.parse("tel:0771442397"));

            startActivity(dialIntent);
        });

        // Add DetailFragment at runtime (only the first time)

        if (savedInstanceState == null) {

            FragmentManager fragmentManager =
                    getSupportFragmentManager();

            FragmentTransaction transaction =
                    fragmentManager.beginTransaction();

            DetailFragment detailFrag =
                    new DetailFragment();

            transaction.add(
                    R.id.dynamic_fragment_container,
                    detailFrag
            );

            transaction.commit();
        }

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
        else if (id == R.id.action_about) {

            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("This is my Lab 03 Android Application.")
                    .setPositiveButton("OK", null)
                    .show();

            return true;
        }
     else if (id == R.id.action_swap) {

        Fragment current = getSupportFragmentManager()
                .findFragmentById(R.id.dynamic_fragment_container);

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        if (current instanceof DetailFragment) {

            transaction.replace(
                    R.id.dynamic_fragment_container,
                    new InfoFragment()
            );

        } else {

            transaction.replace(
                    R.id.dynamic_fragment_container,
                    new DetailFragment()
            );
        }

        transaction.commit();

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