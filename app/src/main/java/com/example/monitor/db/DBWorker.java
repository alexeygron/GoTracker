package com.example.monitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBWorker {

    private Context mContext;
    private SQLiteDatabase mBase;
    private DBHelper mHelper;
    private String mTableName;
    private String mColumnName;

    public DBWorker(Context context, String tableName){
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

    public String[] read() {
        Cursor cursor = mBase.query(mTableName, null, null, null, null, null, null);
        Log.d("FROM_DB_COL_COUNT", " " + cursor.getCount());
        String[] mas = new String[cursor.getCount()];
        int index = 0;
        if (cursor.moveToFirst()) {
            int ipColIndex = cursor.getColumnIndex(mColumnName);
            do {
                Log.d("FROM_DB_VALUE", " " + "ip = " + cursor.getString(ipColIndex));
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
