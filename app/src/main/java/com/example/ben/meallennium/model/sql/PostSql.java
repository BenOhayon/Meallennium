package com.example.ben.meallennium.model.sql;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.sql.room_db_wrapper.MillenniumDatabase;

import java.util.List;

public class PostSql {

    public static List<Post> getAllPosts() {
        return MillenniumDatabase.db.postDao().getAllPosts();
    }

    public static void addPosts(List<Post> list) {
        for (Post p : list) {
            MillenniumDatabase.db.postDao().addPost(p);
        }
    }

    public static void addPost(Post post) {
        MillenniumDatabase.db.postDao().addPost(post);
    }

    public static void deletePost(Post postToDelete) {
        MillenniumDatabase.db.postDao().deletePost(postToDelete);
    }

}
