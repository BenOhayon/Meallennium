package com.example.ben.meallennium.model.entities;

import java.util.Random;

public class User {

    private static final int ID_LENGTH = 7;

    private String id;
    private String password;
    private String email;
    private String username;

    public User(String email, String password) {
        this.id = generateUserId();
        this.email = email;
        this.password = password;
        this.username = extractUsernameFromEmail();
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

    private String extractUsernameFromEmail() {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        String email = getEmail();

        while(i < email.length() && email.charAt(i) != '@') {
            sb.append(email.charAt(i));
            i++;
        }

        return sb.toString();
    }

    public String getUsername() {
        return username;
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
