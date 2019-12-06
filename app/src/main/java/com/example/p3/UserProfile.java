package com.example.p3;

public class UserProfile {

    public String name;
    public String email;

    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

}

