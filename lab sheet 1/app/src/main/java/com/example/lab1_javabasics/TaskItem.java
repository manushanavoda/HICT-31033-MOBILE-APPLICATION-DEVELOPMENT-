package com.example.lab1_javabasics;

public class TaskItem {

    private int id;
    private String title;
    private boolean isCompleted;
    public TaskItem(int id, String title) {
        this.id = id;
        this.title = title;
        this.isCompleted = false;
    }
    public String getTitle() {
        return title;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getSummary() {
        if (isCompleted) {
            return "[DONE] Task #" + id + ": " + title;
        } else {
            return "[PENDING] Task #" + id + ": " + title;
        }
    }

}
