package com.example.monitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.monitor.servers.ServerModel;

import com.example.monitor.utils.Helpers;
import com.lotr.steammonitor.app.R;

import java.util.ArrayList;

public class ServersDao {

    private Context mContext;
    private static final String TAG = Helpers.makeLogTag(ServersDao.class);
    private static final String TABLE_MANE = "favorites";

    public ServersDao(Context context){
        mContext = context;
    }

    public void insert (String value){
        SQLiteDatabase db = new DBHelper(mContext, TABLE_MANE).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ip", value);
        db.insert(TABLE_MANE, null, cv);
        db.close();
    }

    public void delete (String id){
        SQLiteDatabase db = new DBHelper(mContext, TABLE_MANE).getWritableDatabase();
        db.delete(TABLE_MANE, "id=" + id, null);
        db.close();
    }

    public ArrayList<ServerModel> get() {
        SQLiteDatabase db = new DBHelper(mContext, TABLE_MANE).getWritableDatabase();
        ArrayList data = new ArrayList();
        Cursor cursor = db.query(TABLE_MANE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int ipIndex = cursor.getColumnIndex("ip");
            int idIndex = cursor.getColumnIndex("id");
            do {
                ServerModel server = new ServerModel();
                //Log.d(TAG, " " + "ip = " + cursor.getString(ipIndex));
                //Log.d(TAG, " " + "id = " + cursor.getString(idIndex));
                server.setIpAddr(cursor.getString(ipIndex));
                server.setDbId(cursor.getString(idIndex));
                data.add(server);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    static public void insertSet(SQLiteDatabase db, String[] array){
        ContentValues cv = new ContentValues();
        for(String ip: array){
            cv.put("ip", ip);
            db.insert(TABLE_MANE, null, cv);
        }
    }

    public void clear(){
        SQLiteDatabase db = new DBHelper(mContext, TABLE_MANE).getWritableDatabase();
        db.delete(TABLE_MANE, null, null);
        db.close();
    }
}
