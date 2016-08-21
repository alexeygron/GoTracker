package com.example.monitor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Содержит вспомогательные методы для логгирования
 */
public class Helpers {

    private static final String PREFIX = "_";

    /**
     * Возвращает имя класса с префиксом
     */
    public static String makeLogTag(Class className){
        return PREFIX + className.getSimpleName();
    }

    /**
     *  Проверяет наличие соединения с интернетом.
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else {
            return true;
        }
    }
}
