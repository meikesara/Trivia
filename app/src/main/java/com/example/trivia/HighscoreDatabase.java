package com.example.trivia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighscoreDatabase extends SQLiteOpenHelper {

    // Create variable
    private static HighscoreDatabase highscoreDatabase;

    // Create constructor
    public HighscoreDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create table when app is created
        String create = "CREATE TABLE highscore_table (_id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT NOT NULL, score INTEGER NOT NULL);";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "highscore_table");
        onCreate(db);
    }

    public static HighscoreDatabase getInstance(Context context) {

        // Create a new HighscoreDatabase if it has not been created yet
        if (highscoreDatabase == null) {
            highscoreDatabase = new HighscoreDatabase(context, "highscore_table", null, 2);
        }
        return highscoreDatabase;
    }

    public Cursor selectAll() {

        // Create Cursor from all the data in the database
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM highscore_table ORDER BY score DESC", null);
        return cursor;
    }

    public void insert(HighscoreItem highscore) {

        // Get the database
        SQLiteDatabase db = getWritableDatabase();

        // Create new ContentValues and put in the journal variables
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", highscore.getUsername());
        contentValues.put("score", highscore.getScore());

        // Insert contentValues into the database
        db.insert("highscore_table", null, contentValues);
    }

    // This method removes all data from the table
    public void delete() {
        getWritableDatabase().delete("highscore_table","1", null);
    }
}
