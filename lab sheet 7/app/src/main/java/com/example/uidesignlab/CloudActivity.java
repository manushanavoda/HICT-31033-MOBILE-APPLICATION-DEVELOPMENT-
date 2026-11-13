
package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CloudActivity extends AppCompatActivity {

    private EditText editUserId, editName, editRole;
    private Button btnSaveUser, btnUpdateRole, btnDeleteUser;
    private TextView txtCloudData;

    private DatabaseReference databaseReference;
    private ValueEventListener usersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);

        // Connect UI elements
        editUserId = findViewById(R.id.editUserId);
        editName = findViewById(R.id.editName);
        editRole = findViewById(R.id.editRole);

        btnSaveUser = findViewById(R.id.btnSaveUser);
        btnUpdateRole = findViewById(R.id.btnUpdateRole);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);

        txtCloudData = findViewById(R.id.txtCloudData);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance()
                .getReference();

        // Save button
        btnSaveUser.setOnClickListener(v -> saveUser());

        // Update button
        btnUpdateRole.setOnClickListener(v -> updateUserRole());

        // Delete button
        btnDeleteUser.setOnClickListener(v -> deleteUser());

        // Set initial text
        txtCloudData.setText(getString(R.string.no_users));

        // Firebase read listener
        usersListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StringBuilder data = new StringBuilder();

                for (DataSnapshot child : snapshot.getChildren()) {

                    User user = child.getValue(User.class);

                    if (user != null) {

                        String id = child.getKey();

                        data.append(id)
                                .append(": ")
                                .append(user.getName())
                                .append(" (")
                                .append(user.getRole())
                                .append(")\n");
                    }
                }

                if (data.length() == 0) {
                    txtCloudData.setText(
                            getString(R.string.no_users));
                } else {
                    txtCloudData.setText(data.toString());
                }

                Log.d("CloudLab", "Data changed: " + data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("CloudLab",
                        "Failed to read data",
                        error.toException());

                Toast.makeText(CloudActivity.this,
                        "Failed to read data",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("users")
                .addValueEventListener(usersListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        databaseReference.child("users")
                .removeEventListener(usersListener);
    }

    // Save User
    private void saveUser() {

        String userId = editUserId.getText().toString().trim();
        String name = editName.getText().toString().trim();
        String role = editRole.getText().toString().trim();

        if (userId.isEmpty() || name.isEmpty() || role.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        User newUser = new User(name, role);

        databaseReference.child("users")
                .child(userId)
                .setValue(newUser)

                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "Saved to the cloud",
                            Toast.LENGTH_SHORT).show();

                    Log.d("CloudLab", "User saved: " + userId);
                })

                .addOnFailureListener(e -> {

                    Log.w("CloudLab", "Save failed", e);

                    Toast.makeText(this,
                            "Save failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    // Update User Role
    private void updateUserRole() {

        String userId = editUserId.getText().toString().trim();
        String newRole = editRole.getText().toString().trim();

        if (userId.isEmpty() || newRole.isEmpty()) {

            Toast.makeText(this,
                    "Enter User ID and New Role",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        databaseReference.child("users")
                .child(userId)
                .child("role")
                .setValue(newRole)

                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "User role updated successfully",
                            Toast.LENGTH_SHORT).show();
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(this,
                            "Update failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    // Delete User
    private void deleteUser() {

        String userId = editUserId.getText().toString().trim();

        if (userId.isEmpty()) {

            Toast.makeText(this,
                    "Enter User ID",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        databaseReference.child("users")
                .child(userId)
                .removeValue()

                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "User deleted successfully",
                            Toast.LENGTH_SHORT).show();
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(this,
                            "Delete failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}
