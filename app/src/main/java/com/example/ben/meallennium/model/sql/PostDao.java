package com.example.ben.meallennium.model.sql;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.ben.meallennium.model.entities.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from Post")
    List<Post> getAllPosts();

    @Query("select * from Post where Publisher = :publisher")
    List<Post> getPostsByPublisher(String publisher);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPost(Post...post);

    @Delete
    void deletePost(Post post);
}
