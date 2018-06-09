package com.example.ben.meallennium.model.sql;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.sql.room_db_wrapper.MillenniumDatabase;

import java.util.List;

public class PostSql {

    public static List<Post> getAllPosts() {
        return MillenniumDatabase.db.postDao().getAllPosts();
    }

    public static void addPosts(Post...list) {
        for (Post p : list) {
            MillenniumDatabase.db.postDao().addPosts(p);
        }
    }

    public static void deletePost(Post postToDelete) {
        MillenniumDatabase.db.postDao().deletePost(postToDelete);
    }

}
