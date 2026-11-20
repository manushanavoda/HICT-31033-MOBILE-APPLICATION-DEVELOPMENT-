
package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements HeaderFragment.OnHeaderInteractionListener {

    private static final String TAG = "LifecycleTracker";
    private static final String STORAGE_TAG = "StorageLab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.d(TAG, "Dashboard onCreate");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.dashboard_title);

        String userName = getIntent().getStringExtra("USER_NAME");

        if (userName == null) {
            userName = "Student";
        }

        TextView txtUserName = findViewById(R.id.txtUserName);

        txtUserName.setText(
                getString(R.string.hello_user, userName)
        );

        // SharedPreferences visit counter
        if (savedInstanceState == null) {

            SharedPreferences prefs =
                    getPreferences(MODE_PRIVATE);

            int visits = prefs.getInt("visit_count", 0) + 1;

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("visit_count", visits);

            boolean saved = editor.commit();

            Log.d(STORAGE_TAG,
                    "Visit count saved: " + saved + " (" + visits + ")");

            Toast.makeText(
                    this,
                    getString(R.string.visit_count, visits),
                    Toast.LENGTH_SHORT
            ).show();
        }

        // Get user age
        int age = getIntent().getIntExtra("USER_AGE", 0);

        Toast.makeText(
                this,
                "Your age is " + age,
                Toast.LENGTH_SHORT
        ).show();

        // RecyclerView
        RecyclerView recyclerView =
                findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

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

        MyAdapter myAdapter = new MyAdapter(items, item -> {
            updateDetail(item);
        });

        recyclerView.setAdapter(myAdapter);

        // Dial button
        Button btnWebsite = findViewById(R.id.btnWebsite);

        btnWebsite.setOnClickListener(v -> {

            Intent dialIntent =
                    new Intent(Intent.ACTION_DIAL);

            dialIntent.setData(
                    Uri.parse("tel:0771442397")
            );

            startActivity(dialIntent);
        });

        // Add DetailFragment
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

    // Create toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.dashboard_menu,
                menu
        );

        return true;
    }

    // Handle menu clicks
    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_notes) {

            startActivity(
                    new Intent(this, NotesActivity.class)
            );

            return true;

        } else if (id == R.id.action_logout) {

            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        // Clear Remember Me data
                        getSharedPreferences(
                                MainActivity.PREFS_NAME,
                                MODE_PRIVATE
                        ).edit().clear().apply();

                        // Reset visit counter
                        getPreferences(MODE_PRIVATE)
                                .edit()
                                .remove("visit_count")
                                .apply();

                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;

        } else if (id == R.id.action_about) {

            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage(
                            "This is my Lab 03 Android Application."
                    )
                    .setPositiveButton("OK", null)
                    .show();

            return true;

        } else if (id == R.id.action_swap) {

            Fragment current =
                    getSupportFragmentManager()
                            .findFragmentById(
                                    R.id.dynamic_fragment_container
                            );

            FragmentTransaction transaction =
                    getSupportFragmentManager()
                            .beginTransaction();

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

            transaction.addToBackStack(null);
            transaction.commit();

            return true;

        } else if (id == R.id.action_users) {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    UsersActivity.class
            );

            startActivity(intent);

            return true;

        } else if (id == R.id.action_cloud) {

            // Open Firebase Cloud Users screen
            Intent intent = new Intent(
                    DashboardActivity.this,
                    CloudActivity.class
            );

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // HeaderFragment callback
    @Override
    public void onButtonClicked(String data) {

        updateDetail(data);
    }

    @Override
    public void onClearButtonClicked() {

        updateDetail(getString(R.string.detail_default));
    }

    // Update DetailFragment
    private void updateDetail(String text) {

        Fragment current =
                getSupportFragmentManager()
                        .findFragmentById(
                                R.id.dynamic_fragment_container
                        );

        if (current instanceof DetailFragment) {

            ((DetailFragment) current).updateTextView(text);

        } else {

            Toast.makeText(
                    this,
                    "Detail area is not showing",
                    Toast.LENGTH_SHORT
            ).show();
        }
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
