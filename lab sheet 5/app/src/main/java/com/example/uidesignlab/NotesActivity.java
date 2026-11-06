
package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "StorageLab";
    private static final String FILE_NAME = "notes.txt";

    private EditText editNote;
    private TextView txtFileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        editNote = findViewById(R.id.editNote);
        txtFileInfo = findViewById(R.id.txtFileInfo);

        Button btnSaveNote = findViewById(R.id.btnSaveNote);
        Button btnLoadNote = findViewById(R.id.btnLoadNote);
        Button btnDeleteNote = findViewById(R.id.btnDeleteNote);
        Button btnAppendNote = findViewById(R.id.btnAppendNote);

        // Save Button
        btnSaveNote.setOnClickListener(v ->
                saveNote(editNote.getText().toString()));

        // Load Button
        btnLoadNote.setOnClickListener(v -> {
            String saved = loadNote();

            if (saved == null) {
                Toast.makeText(this, "No note saved yet",
                        Toast.LENGTH_SHORT).show();
            } else {
                editNote.setText(saved);
                Toast.makeText(this, "Note loaded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Delete Button
        btnDeleteNote.setOnClickListener(v -> {
            boolean deleted = deleteFile(FILE_NAME);

            editNote.setText("");

            Toast.makeText(this,
                    deleted ? "Note deleted" : "Nothing to delete",
                    Toast.LENGTH_SHORT).show();

            updateFileInfo();
        });

        // Append Button
        btnAppendNote.setOnClickListener(v ->
                appendNote(editNote.getText().toString()));

        updateFileInfo();
        String savedNote = loadNote();

        if (savedNote != null) {
            editNote.setText(savedNote);
        }

    }

    // Save note
    private void saveNote(String text) {

        try (FileOutputStream fos =
                     openFileOutput(FILE_NAME, MODE_PRIVATE)) {

            fos.write(text.getBytes(StandardCharsets.UTF_8));

            Toast.makeText(this, "Note saved",
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            Log.e(TAG, "Could not save the note", e);

            Toast.makeText(this, "Could not save the note",
                    Toast.LENGTH_SHORT).show();
        }

        updateFileInfo();
    }

    // Append note - Question 3
    private void appendNote(String text) {

        if (text.trim().isEmpty()) {
            Toast.makeText(this, "Enter some text",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        File file = getFileStreamPath(FILE_NAME);

        try (FileOutputStream fos =
                     openFileOutput(FILE_NAME, MODE_APPEND)) {

            if (file.exists() && file.length() > 0) {
                fos.write("\n".getBytes(StandardCharsets.UTF_8));
            }

            fos.write(text.getBytes(StandardCharsets.UTF_8));

            Toast.makeText(this, "Note appended",
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            Log.e(TAG, "Could not append the note", e);

            Toast.makeText(this, "Append failed",
                    Toast.LENGTH_SHORT).show();
        }

        updateFileInfo();
    }

    // Load note
    private String loadNote() {

        StringBuilder text = new StringBuilder();

        try (FileInputStream fis = openFileInput(FILE_NAME);
             InputStreamReader isr =
                     new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

        } catch (FileNotFoundException e) {

            return null;

        } catch (IOException e) {

            Log.e(TAG, "Could not read the note", e);

            return null;
        }

        return text.toString();
    }

    // Update file information
    private void updateFileInfo() {

        File file = getFileStreamPath(FILE_NAME);

        if (file.exists()) {

            txtFileInfo.setText(
                    getString(R.string.file_info,
                            FILE_NAME, file.length()));

        } else {

            txtFileInfo.setText(R.string.file_none);
        }
    }
}
