package com.example.ben.meallennium.model.entities;

import java.util.Random;

public class Post {

    private static final int ID_LENGTH = 12;

    private String id;
    private String name;
    private String description;

    public Post() {}

    public Post(String name, String description) {
        this.id = generatePostId();
        this.name = name;
        this. description = description;
    }

    private String generatePostId() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0 ; i < ID_LENGTH ; i++) {
            int c = rand.nextInt(10);
            sb.append(c);
        }

        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
