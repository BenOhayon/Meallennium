package com.example.ben.meallennium.model.sql;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.sql.room_db_wrapper.PostAsyncDao;

import java.util.List;

public class SqlModel {

    // ----------------------
    // Post table operations
    // ----------------------

    public void getAllPosts(PostAsyncDao.PostAsyncDaoListener<List<Post>> listener) {
        PostAsyncDao.getAllPosts(listener);
    }

    public void addPost(Post postToAdd, PostAsyncDao.PostAsyncDaoListener<Boolean> listener) {
        PostAsyncDao.addPost(postToAdd, listener);
    }

    public void deletePost(Post postToDelete, PostAsyncDao.PostAsyncDaoListener<Boolean> listener) {
        PostAsyncDao.deletePost(postToDelete, listener);
    }
}
