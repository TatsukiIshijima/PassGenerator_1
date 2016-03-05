package io.tatsuki.password_generator_ver10;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TI on 2015/09/22.
 */
public class MyDatabasehelper extends SQLiteOpenHelper {
    static final String ID ="id";
    static final String DATABASE_NAME = "passworddatabase.db";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "pass_table";
    static final String PASSWORD = "password";
    static final String MEMO = "memo";
    static final String DATETIME = "datetime";

    public MyDatabasehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY,"+  PASSWORD + " TEXT," + MEMO + " TEXT," + DATETIME + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
