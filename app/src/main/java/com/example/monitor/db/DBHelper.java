package com.example.monitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.monitor.utils.Helpers;
import com.lotr.steammonitor.app.R;

/**
 * Manages open, create and upgrade DB
 */
class DBHelper extends SQLiteOpenHelper {

    private String mTable;
    private Context mContext;

    private static final String CREATE_FAVORITES_TABLE = "create table favorites (" +
            "id integer primary key autoincrement," + "ip text," + "pos integer" + ");";
    private static final String CREATE_PLAYERS_TABLE = "create table players (" +
            "label integer primary key autoincrement," + "id text" + ");";

    private static final String TAG = Helpers.makeLogTag(DBHelper.class);
    private static final int DB_VERSION = 3;

    public DBHelper(Context context, String table) {
        super(context, table, null, DB_VERSION);
        mTable = table;
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (mTable.equals("favorites")) {
            db.execSQL(CREATE_FAVORITES_TABLE);
            ServersDao.insertSet(db, mContext.getResources().getStringArray(R.array.ips));
        } else if (mTable.equals("players")) {
            db.execSQL(CREATE_PLAYERS_TABLE);
            PlayersDao.insertSet(db, mContext.getResources().getStringArray(R.array.players));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 3) {
            upgradePlayersTable(db);
        }
    }

    private void upgradePlayersTable(SQLiteDatabase db) {
        Cursor cursor = db.query(PlayersDao.TABLE_NAME, null, null, null, null, null, null);
        db.execSQL("DROP TABLE IF EXISTS " + PlayersDao.TABLE_NAME);
        db.execSQL(CREATE_PLAYERS_TABLE);

        ContentValues cv = new ContentValues();
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex("id");
            do {
                cv.put("id", cursor.getString(id));
                db.insert(PlayersDao.TABLE_NAME, null, cv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
}


