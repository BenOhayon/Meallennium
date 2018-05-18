package com.example.ben.meallennium.model.entities;

import java.util.Random;

public class User {

    private static final int ID_LENGTH = 12;

    private String id;
    private String hashedPassword;

    public User(String password) {
        this.id = generateUserId();
        this.hashedPassword = password;
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

    public String getId() {
        return this.id;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
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
