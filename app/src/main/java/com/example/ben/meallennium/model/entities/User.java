package com.example.ben.meallennium.model.entities;

import java.util.Random;

public class User {

    private static final int ID_LENGTH = 7;

    private String id;
    private String password;
    private String email;

    public User(String email, String password) {
        this.id = generateUserId();
        this.email = email;
        this.password = password;
    }

    private String generateUserId() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0 ; i < ID_LENGTH ; i++) {
            int c = rand.nextInt(10);
            sb.append(c);
        }

        return sb.toString();
    }

    public String getId() {
        return this.id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return this.id + "," + this.password;
    }
}
