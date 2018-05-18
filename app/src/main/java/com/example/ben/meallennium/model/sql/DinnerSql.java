package com.example.ben.meallennium.model.sql;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.ben.meallennium.model.entities.User;

public class DinnerSql {
    public static final String TABLE_NAME = "Users";

    public static final String ID_COLUMN = "dinnerId";
    public static final String EMAIL_COLUMN = "email";
    public static final String PASSWORD_COLUMN = "password";

    private static final String CREATE_DINNERS_TABLE_QUERY = "create table " + TABLE_NAME
            + " (" + ID_COLUMN + " text primary key, " + EMAIL_COLUMN + " text, "
            + PASSWORD_COLUMN + " text);";

    private static final String DROP_DINNERS_TABLE_QUERY = "drop table " + TABLE_NAME + ";";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_DINNERS_TABLE_QUERY);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL(DROP_DINNERS_TABLE_QUERY);
    }

    public static void addUser(User user, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, user.getId());
        values.put(PASSWORD_COLUMN, user.getHashedPassword());
        db.insert(TABLE_NAME, null, values);
    }
}
