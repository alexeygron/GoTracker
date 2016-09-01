package com.example.monitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.monitor.players.PlayerModel;
import com.example.monitor.utils.Helpers;

import java.util.ArrayList;

public class PlayersDao {

    private Context mContext;

    private static final String TAG = Helpers.makeLogTag(PlayersDao.class);
    public static final String TABLE_NAME = "players";

    public PlayersDao(Context context){
        mContext = context;
    }

    public void insert (String value){
        SQLiteDatabase db = new DBHelper(mContext, TABLE_NAME).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", value);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    static public void insertSet(SQLiteDatabase db, String[] array) {
        ContentValues cv = new ContentValues();
        for (String id : array) {
            cv.put("id", id);
            db.insert(TABLE_NAME, null, cv);
        }
    }

    public void delete (String label){
        SQLiteDatabase db = new DBHelper(mContext, TABLE_NAME).getWritableDatabase();
        db.delete(TABLE_NAME, "label=" + label, null);
        db.close();
    }

    public ArrayList<PlayerModel> get() {
        SQLiteDatabase db = new DBHelper(mContext, TABLE_NAME).getWritableDatabase();
        ArrayList<PlayerModel> data = new ArrayList();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex("id");
            int label = cursor.getColumnIndex("label");
            do {
                PlayerModel player = new PlayerModel();
                Log.d(TAG, " " + "id = " + cursor.getString(id));
                player.setSteamID(cursor.getString(id));
                Log.d(TAG, " " + "label = " + cursor.getString(label));
                player.setDbLabel(cursor.getString(label));
                data.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    public void clear(){
        SQLiteDatabase db = new DBHelper(mContext, TABLE_NAME).getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
