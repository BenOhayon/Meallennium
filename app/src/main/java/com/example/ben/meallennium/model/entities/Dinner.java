package com.example.ben.meallennium.model.entities;

public class Dinner extends User {

    private String username;

    public Dinner(String email, String password) {
        super(email, password);
        this.username = extractUsernameFromEmail();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
