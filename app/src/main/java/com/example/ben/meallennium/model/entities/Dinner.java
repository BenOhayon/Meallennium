package com.example.ben.meallennium.model.entities;

public class Dinner extends User {

    private String email;

    public Dinner(String email, String password) {
        super(password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
