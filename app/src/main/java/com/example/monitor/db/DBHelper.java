package com.example.monitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper for db.
 */
class DBHelper extends SQLiteOpenHelper {

    private String mTable;
    private static final String CREATE_FAVORITES_TABLE = "create table favorites (" +
            "id integer primary key autoincrement," + "ip text," + "pos integer" + ");";
    private static final String CREATE_PLAYERS_TABLE = "create table players (" + "id text" + ");";

    public DBHelper(Context context, String table) {
        super(context, table, null, 1);
        mTable = table;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (mTable.equals("favorites")){
            db.execSQL(CREATE_FAVORITES_TABLE);
        } else if (mTable.equals("players")) {
            db.execSQL(CREATE_PLAYERS_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


