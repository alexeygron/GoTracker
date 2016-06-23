/* Класс использующийся для доступа к базе данных SQLite*/

package com.lotr.steammonitor.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {

    String mTable;

    public DBHelper(Context context, String table) {
      // конструктор суперкласса
        super(context, table, null, 1);
        mTable = table;
    }

// метод, который будет вызван, если БД, к которой мы хотим подключиться – не существует
    @Override
    public void onCreate(SQLiteDatabase db) {
      Log.d("работа с ДБ", "--- onCreate database ---");

        if (mTable == "favorites"){

      // создаем таблицу с полями
      db.execSQL("create table favorites ("
          + "id integer primary key autoincrement," 
          + "ip text,"
          + "pos integer" + ");");
    }
        else {
            // создаем таблицу с полями
            db.execSQL("create table players ("
                    + "id text" + ");");

        }
}

// будет вызван в случае, если мы пытаемся подключиться к БД более новой версии, чем существующая
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
  }


