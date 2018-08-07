package com.example.ben.meallennium.model.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Random;

@Entity
public class Post implements Serializable{

    private static final int ID_LENGTH = 12;

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "Name")
    @NonNull
    private String name;

    @ColumnInfo(name = "Description")
    private String description;

    @ColumnInfo(name = "Image URL")
    private String imageUrl;

    @ColumnInfo(name = "Publisher")
    private String publisher;

    @Ignore
    public Post() {}

    @Ignore
    public Post(String publisher, String name, String description) {
        this(publisher, name, description, null);
    }

    public Post(String publisher, @NonNull String name, String description, String imageUrl) {
        this.publisher = publisher;
        this.id = generatePostId();
        this.name = name;
        this. description = description;
        this.imageUrl = imageUrl;
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

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Post) {
            Post post = (Post) obj;

            return this.id.equals(post.id) &&
                    this.name.equals(post.name) &&
                    this.description.equals(post.description) &&
                    ((this.imageUrl == null && post.imageUrl == null) || (this.imageUrl != null && post.imageUrl != null && this.imageUrl.equals(post.imageUrl)));
        } else return false;
    }
}
