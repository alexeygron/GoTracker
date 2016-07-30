package com.example.monitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.monitor.servers.ServerModel;

import com.example.monitor.utils.LogUtils;

import java.util.ArrayList;

public class ServersDao {

    private Context mContext;
    private SQLiteDatabase mBase;
    private DBHelper mHelper;

    private static final String TAG = LogUtils.makeLogTag(ServersDao.class);
    private static final String TABLE_MANE = "favorites";

    public ServersDao(Context context){
        mContext = context;
        mHelper = new DBHelper(mContext, TABLE_MANE);
        mBase = mHelper.getWritableDatabase();
    }

    public void insert (String value){
        ContentValues cv = new ContentValues();
        cv.put("ip", value);
        mBase.insert(TABLE_MANE, null, cv);
    }

    public void delete (String id){
       // mBase.delete(mTableName, position "ip" + "='" + ipAddr + "'", null);
        mBase.delete(TABLE_MANE, "id=" + id, null);
    }

    public ArrayList<ServerModel> readToList() {
        SQLiteDatabase mBase = new DBHelper(mContext, TABLE_MANE).getWritableDatabase();

        ArrayList data = new ArrayList();
        Cursor cursor = mBase.query(TABLE_MANE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int ipIndex = cursor.getColumnIndex("ip");
            int idIndex = cursor.getColumnIndex("id");
            do {
                ServerModel server = new ServerModel();
                Log.d(TAG, " " + "ip = " + cursor.getString(ipIndex));
                Log.d(TAG, " " + "id = " + cursor.getString(idIndex));
                server.setIpAddr(cursor.getString(ipIndex));
                server.setDbId(cursor.getString(idIndex));
                data.add(server);
            } while (cursor.moveToNext());
        }
        return data;
    }

    public void close(){
        mBase.close();
    }

    public void clear(){
        mBase.delete(TABLE_MANE, null, null);
    }
}
