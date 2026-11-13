package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CloudActivity extends AppCompatActivity {

    private EditText editUserId, editName, editRole, editEmail;
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
        editEmail = findViewById(R.id.editEmail);

        btnSaveUser = findViewById(R.id.btnSaveUser);
        btnUpdateRole = findViewById(R.id.btnUpdateRole);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);

        txtCloudData = findViewById(R.id.txtCloudData);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance()
                .getReference();

        // Button click listeners
        btnSaveUser.setOnClickListener(v -> saveUser());

        btnUpdateRole.setOnClickListener(v -> updateUserRole());

        btnDeleteUser.setOnClickListener(v -> deleteUser());

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

                        String email = user.getEmail();

                        if (email == null) {
                            email = "";
                        }

                        data.append(id)
                                .append(": ")
                                .append(user.getName())
                                .append(" (")
                                .append(user.getRole())
                                .append(") - ")
                                .append(email)
                                .append("\n");
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

    // Task 1 + Task 4: Save User with Email and Automatic ID
    private void saveUser() {

        String name = editName.getText().toString().trim();
        String role = editRole.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (name.isEmpty() || role.isEmpty() || email.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        // Generate unique Firebase ID
        DatabaseReference usersRef =
                databaseReference.child("users");

        String newId = usersRef.push().getKey();

        if (newId == null) {

            Toast.makeText(this,
                    "Failed to generate ID",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        // Create User object
        User newUser = new User(name, role, email);

        // Save to Firebase
        usersRef.child(newId).setValue(newUser)

                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "Saved to cloud. ID: " + newId,
                            Toast.LENGTH_LONG).show();

                    Log.d("CloudLab",
                            "User saved: " + newId);

                    editUserId.setText("");
                    editName.setText("");
                    editRole.setText("");
                    editEmail.setText("");
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(this,
                            "Save failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    Log.w("CloudLab", "Save failed", e);
                });
    }

    // Task 2: Update Existing User Role
    private void updateUserRole() {

        String userId = editUserId.getText().toString().trim();
        String newRole = editRole.getText().toString().trim();

        if (userId.isEmpty() || newRole.isEmpty()) {

            Toast.makeText(this,
                    "Enter User ID and New Role",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        DatabaseReference userRef =
                databaseReference.child("users").child(userId);

        userRef.get().addOnSuccessListener(snapshot -> {

            if (snapshot.exists()) {

                userRef.child("role")
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

            } else {

                Toast.makeText(this,
                        "User does not exist",
                        Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(e -> {

            Toast.makeText(this,
                    "Read failed: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        });
    }

    // Task 3: Delete User with Confirmation Dialog
    private void deleteUser() {

        String userId = editUserId.getText().toString().trim();

        if (userId.isEmpty()) {

            Toast.makeText(this,
                    "Enter User ID",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        new AlertDialog.Builder(this)

                .setTitle("Delete User")

                .setMessage("Are you sure you want to delete this user?")

                .setPositiveButton("Yes", (dialog, which) -> {

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
                })

                .setNegativeButton("No", null)

                .show();
    }
}
