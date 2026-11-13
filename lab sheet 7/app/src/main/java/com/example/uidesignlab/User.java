
package com.example.uidesignlab;

public class User {

    private String name;
    private String role;
    private String email;

    // Empty constructor required by Firebase
    public User() {
    }

    // Constructor with name, role and email
    public User(String name, String role, String email) {
        this.name = name;
        this.role = role;
        this.email = email;
    }

    // Keep this constructor for older code
    public User(String name, String role) {
        this(name, role, "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
