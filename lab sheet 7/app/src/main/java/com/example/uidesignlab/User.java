package com.example.uidesignlab;



public class User {

    private String name;
    private String role;

    // Empty constructor required by Firebase
    public User() {
    }

    // Constructor with parameters
    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
