
package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.database.Cursor;
import android.net.Uri;
import android.widget.TextView;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;

    private static final int CONTACT_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        recyclerViewUsers.setLayoutManager(
                new LinearLayoutManager(this)
        );

        userAdapter = new UserAdapter();
        recyclerViewUsers.setAdapter(userAdapter);

        // Add John
        Button btnAddJohn = findViewById(R.id.btnAddJohn);

        btnAddJohn.setOnClickListener(v -> {

            AppDatabase.databaseExecutor.execute(() -> {

                AppDatabase.getInstance(getApplicationContext())
                        .userDao()
                        .insert(new UserRecord("John"));

                runOnUiThread(this::loadUsers);
            });
        });

        // Clear All
        Button btnClearAll = findViewById(R.id.btnClearAll);

        btnClearAll.setOnClickListener(v -> {

            AppDatabase.databaseExecutor.execute(() -> {

                AppDatabase.getInstance(getApplicationContext())
                        .userDao()
                        .deleteAll();

                runOnUiThread(this::loadUsers);
            });
        });

        // Read Provider
        Button btnReadProvider = findViewById(R.id.btnReadProvider);

        btnReadProvider.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        CONTACT_PERMISSION_CODE
                );

            } else {
                readContacts();
            }
        });
    }

    // Load database users
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

    // Read Android Contacts Provider
    private void readContacts() {

        StringBuilder contactList = new StringBuilder();

        ContentResolver resolver = getContentResolver();

        try (Cursor cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )) {

            if (cursor != null && cursor.moveToFirst()) {

                do {

                    String name = cursor.getString(0);
                    String phone = cursor.getString(1);

                    contactList.append(name)
                            .append(" - ")
                            .append(phone)
                            .append("\n");

                } while (cursor.moveToNext());

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Phone Contacts")
                        .setMessage(contactList.toString())
                        .setPositiveButton("OK", null)
                        .show();

            } else {

                Toast.makeText(
                        this,
                        "No contacts found",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (Exception e) {

            Log.e("ContactsProvider", "Error reading contacts", e);

            Toast.makeText(
                    this,
                    "Unable to read contacts",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Permission result
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == CONTACT_PERMISSION_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                readContacts();

            } else {

                Toast.makeText(
                        this,
                        "Contacts permission denied",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadUsers();

        readUsersFromProvider();
    }

    private void readUsersFromProvider() {

        AppDatabase.databaseExecutor.execute(() -> {

            Uri uri = UserProvider.CONTENT_URI;

            try (Cursor cursor = getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    "id DESC"
            )) {

                if (cursor != null) {

                    while (cursor.moveToNext()) {

                        int id = cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                        String name = cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        );

                        Log.d("ContentProvider",
                                "ID: " + id + ", Name: " + name);
                    }

                    Log.d("ContentProvider",
                            "Users read successfully");

                }

            } catch (Exception e) {

                Log.e("ContentProvider",
                        "Error reading users", e);
            }
        });
    }

}
