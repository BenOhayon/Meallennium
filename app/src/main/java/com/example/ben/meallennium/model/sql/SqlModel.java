package com.example.ben.meallennium.model.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ben.meallennium.model.entities.Dinner;
import com.example.ben.meallennium.model.entities.User;

public class SqlModel {

    private static final String DATABASE_NAME = "Meallennium.db";
    private static final int DATABASE_VERSION = 1;

    private MeallenniumHelper helper;

    public SqlModel(Context context) {
        helper = new MeallenniumHelper(context);
    }

    public void addUserEntry(User user) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(user instanceof Dinner) {
            values.put(DinnerSql.ID_COLUMN, user.getId());
            values.put(DinnerSql.PASSWORD_COLUMN, user.getPassword());
            values.put(DinnerSql.EMAIL_COLUMN, ((Dinner) user).getEmail());
        }

        db.insert(DinnerSql.TABLE_NAME, null, values);
    }

    public void popUpAllUserEntries() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DinnerSql.TABLE_NAME, null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(DinnerSql.ID_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(DinnerSql.PASSWORD_COLUMN));
                String email = cursor.getString(cursor.getColumnIndex(DinnerSql.EMAIL_COLUMN));
                String message = "User: " + id + ", password: " + password + ", email: " + email;
                Log.d("buildTest", message);
            } while(cursor.moveToNext());
        }
    }


    private class MeallenniumHelper extends SQLiteOpenHelper {

        public MeallenniumHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Type the creation of the tables here.
            DinnerSql.createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Type the operations when changing the schema of tables in the database.
            DinnerSql.dropTable(db);
            DinnerSql.createTable(db);
        }
    }

}
