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
}
