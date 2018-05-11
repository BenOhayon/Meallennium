package com.example.ben.meallennium.model.entities;

public class User {

    private String username;
    private int hashedPassword;

    public User(String username, String password) {
        this.username = username;
        this.hashedPassword = calculatePasswordHash(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(int hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    private int calculatePasswordHash(String password) {
        byte[] bytes = new byte[password.length()];

        for(int i = 0 ; i < bytes.length ; i++) {
            bytes[i] = (byte) password.charAt(i);
        }

        // TODO complete the hashing logic.

        return 0;
    }
}
