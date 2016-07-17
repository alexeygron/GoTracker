package com.example.monitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ServersDao {

    private Context mContext;
    private SQLiteDatabase mBase;
    private DBHelper mHelper;
    private String mTableName;
    private String mColumnName;

    private static final String TAG = "db_worker";

    public ServersDao(Context context, String tableName){
        mContext = context;
        mTableName = tableName;
        mHelper = new DBHelper(mContext, tableName);
        mBase = mHelper.getWritableDatabase();

        if (mTableName.equals("favorites")) {
            mColumnName = "ip";
        } else {
            mColumnName = "id";
        }

    }

    public void insert (String value){
        ContentValues cv = new ContentValues();
        cv.put(mColumnName, value);
        mBase.insert(mTableName, null, cv);
    }

    public void delete (String ipAddr, int position){
       // mBase.delete(mTableName, position "ip" + "='" + ipAddr + "'", null);
        mBase.delete(mTableName, "id=" + position, null);
    }

    public String[] read() {
        Cursor cursor = mBase.query(mTableName, null, null, null, null, null, null);
        Log.d(TAG, " to string " + cursor.toString());
        String[] mas = new String[cursor.getCount()];
        int index = 0;
        if (cursor.moveToFirst()) {
            int ipColIndex = cursor.getColumnIndex(mColumnName);
            int ipColIndex2 = cursor.getColumnIndex("id");

            do {
                Log.d(TAG, " " + "ip = " + cursor.getString(ipColIndex));
                Log.d(TAG, " " + "ip = " + cursor.getString(ipColIndex2));
                mas[index] = cursor.getString(ipColIndex);
                index++;
            } while (cursor.moveToNext());
        }
        return mas;
    }

    public void close(){
        mBase.close();
    }

    public void clear(){
        mBase.delete(mTableName, null, null);
    }
}
