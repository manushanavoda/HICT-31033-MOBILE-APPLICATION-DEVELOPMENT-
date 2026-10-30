package com.example.lab1_javabasics;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Week1Lab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Hello from my first app!");

        String name = "Manusha";
        int age = 15;
        Log.i(TAG, "Name: " + name + ", Age: " + age);

        if (age >= 18) {
            Log.i(TAG, "Adult");
        } else {
            Log.i(TAG, "Minor");
        }

        for (int i = 1; i <= 3; i++) {
            Log.i(TAG, "Count: " + i);
        }

        List<TaskItem> tasks = new ArrayList<>();
        tasks.add(new TaskItem(1, "Install Android Studio"));
        tasks.add(new TaskItem(2, "Create my first project"));
        tasks.add(new TaskItem(3, "Run the app on the emulator"));
        tasks.add(new TaskItem(4, "Learn Java"));

        tasks.get(0).markCompleted();
        tasks.get(1).markCompleted();

        Log.i(TAG, "--- My Tasks ---");
        for (TaskItem task : tasks) {
            Log.i(TAG, task.getSummary());
        }

        int doneCount = 0;
        for (TaskItem task : tasks) {
            if (task.isCompleted()) {
                doneCount = doneCount + 1;
            }
        }
        Log.i(TAG, "Done tasks: " + doneCount);


    }
}