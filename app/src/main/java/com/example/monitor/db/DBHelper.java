package com.example.monitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper for db.
 */
class DBHelper extends SQLiteOpenHelper {

    String mTable;

    public DBHelper(Context context, String table) {
        super(context, table, null, 1);
        mTable = table;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (mTable == "favorites"){
            db.execSQL("create table favorites ("
                    + "id integer primary key autoincrement,"
                    + "ip text,"
                    + "pos integer" + ");");
        } else {
            db.execSQL("create table players (" + "id text" + ");");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}


