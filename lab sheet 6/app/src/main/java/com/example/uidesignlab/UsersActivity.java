
package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Find RecyclerView
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        // Set RecyclerView layout
        recyclerViewUsers.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Create adapter
        userAdapter = new UserAdapter();
        recyclerViewUsers.setAdapter(userAdapter);

        // Add John button
        Button btnAddJohn = findViewById(R.id.btnAddJohn);

        btnAddJohn.setOnClickListener(v -> {

            AppDatabase.databaseExecutor.execute(() -> {

                AppDatabase.getInstance(getApplicationContext())
                        .userDao()
                        .insert(new UserRecord("John"));

                runOnUiThread(this::loadUsers);
            });
        });

        // Clear All button
        Button btnClearAll = findViewById(R.id.btnClearAll);

        btnClearAll.setOnClickListener(v -> {

            AppDatabase.databaseExecutor.execute(() -> {

                AppDatabase.getInstance(getApplicationContext())
                        .userDao()
                        .deleteAll();

                runOnUiThread(this::loadUsers);
            });
        });
    }

    private void loadUsers() {

        AppDatabase.databaseExecutor.execute(() -> {

            List<UserRecord> users =
                    AppDatabase.getInstance(getApplicationContext())
                            .userDao()
                            .getAll();

            runOnUiThread(() -> {
                userAdapter.setUsers(users);
            });

            Log.d("RoomLab", "Users loaded: " + users.size());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }
}
