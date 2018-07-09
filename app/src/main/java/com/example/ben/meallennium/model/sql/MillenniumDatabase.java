package com.example.ben.meallennium.model.sql;

import android.arch.persistence.room.Room;

import com.example.ben.meallennium.activities.MeallenniumApplication;

public class MillenniumDatabase {

    public static MillenniumDatabaseRepository db = Room.databaseBuilder(MeallenniumApplication.context,
            MillenniumDatabaseRepository.class,
            "MillenniumDB.db").fallbackToDestructiveMigration().build();

}
