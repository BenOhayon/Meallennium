package com.example.ben.meallennium.model.entities;

import java.util.Random;

public class User {

    private static final int ID_LENGTH = 12;

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
            char c = (char)(rand.nextInt(136) + 40);
            sb.append(c);
        }

        return sb.toString();
    }

    public String extractUsernameFromEmail() {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        while(i < email.length() && email.charAt(i) != '@') {
            sb.append(email.charAt(i));
            i++;
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
