package com.example.student.forum.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Student on 4/12/2017.
 */

import com.example.student.forum.data.ForumContract;

public class ForumDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "forum.db";

    public ForumDbHelper(Context context)
    {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        String SQL_CREATE_ENTRIES = "CREATE table if not exists " + ForumContract.LoginEntry.TABLE_NAME + "("
                + ForumContract.LoginEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ForumContract.LoginEntry.COLUMN_USERNAME + " TEXT NOT NULL,"
                + ForumContract.LoginEntry.COLUMN_PASSWROD + " TEXT NOT NULL,"
                + ForumContract.LoginEntry.COLUMN_EMAIL + " TEXT NOT NULL,"
                + "unique(" + ForumContract.LoginEntry.COLUMN_USERNAME + "));";
        db.execSQL(SQL_CREATE_ENTRIES);

        String CONTENT_ENTRIES = "CREATE TABLE if not exists " + ForumContract.ForumEntry.TABLE_NAME + "("
                + ForumContract.ForumEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ForumContract.ForumEntry.COLUMN_TITLE + " TEXT NOT NULL,"
                + ForumContract.ForumEntry.COLUMN_POST + " TEXT NOT NULL,"
                + ForumContract.ForumEntry.COLUMN_COMMENTS + " TEXT,"
                + ForumContract.ForumEntry.COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + ForumContract.ForumEntry.COLUMN_USER + " TEXT NOT NULL,"
                + ForumContract.ForumEntry.COLUMN_LIKES + " INTEGER NOT NULL,"
                + ForumContract.ForumEntry.COLUMN_TAGS + " TEXT NOT NULL,"
                + ForumContract.ForumEntry.COLUMN_STATUS + " INTEGER NOT NULL,"
                + ForumContract.ForumEntry.COLUMN_REPORT + " INTEGER NOT NULL);";
        db.execSQL(CONTENT_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
