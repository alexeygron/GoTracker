package com.example.monitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.monitor.players.PlayerModel;
import com.example.monitor.utils.Helpers;
import com.lotr.steammonitor.app.R;

/**
 * Helper for db.
 */
class DBHelper extends SQLiteOpenHelper {

    private static final String CREATE_FAVORITES_TABLE = "create table favorites (" +
            "id integer primary key autoincrement," + "ip text," + "pos integer" + ");";
    private static final String CREATE_PLAYERS_TABLE = "create table players (" +
            "label integer primary key autoincrement," + "id text" + ");";
    private static final String TAG = Helpers.makeLogTag(DBHelper.class);
    private String mTable;
    private Context mContext;

    public DBHelper(Context context, String table) {
        super(context, table, null, 2);
        mTable = table;
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (mTable.equals("favorites")) {
            db.execSQL(CREATE_FAVORITES_TABLE);
            ServersDao.insertSet(db, mContext.getResources().getStringArray(R.array.ips));
        } else if (mTable.equals("players")) {
            Log.i(TAG, "Create table players");
            db.execSQL(CREATE_PLAYERS_TABLE);
            PlayersDao.insertSet(db, mContext.getResources().getStringArray(R.array.players));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade " + oldVersion + " to " + newVersion);
        if (newVersion == 2) {
            upgradePlayersTable(db);
        }
    }

    private void upgradePlayersTable(SQLiteDatabase db) {
        String upgradeQuery = "ALTER TABLE " + PlayersDao.TABLE_MANE
                + " ADD COLUMN " + "label" + " integer primary key autoincrement";
        Cursor cursor = db.query(PlayersDao.TABLE_MANE, null, null, null, null, null, null);
        db.delete(PlayersDao.TABLE_MANE, null, null);
        db.execSQL(upgradeQuery);
        ContentValues cv = new ContentValues();
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex("id");
            do {
                cv.put("id", cursor.getString(id));
                db.insert(PlayersDao.TABLE_MANE, null, cv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
}


