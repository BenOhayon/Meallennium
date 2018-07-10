package com.example.ben.meallennium.model.sql;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.ben.meallennium.model.entities.Post;

@Database(entities = {Post.class}, version = 1)
public abstract class MillenniumDatabaseRepository extends RoomDatabase {
    public abstract PostDao postDao();
}
