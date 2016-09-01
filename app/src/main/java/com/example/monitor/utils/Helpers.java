package com.example.monitor.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Содержит вспомогательные методы.
 */
public class Helpers {

    private static final String PREFIX = "_";

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

    public static void openPlayStorePage(Context context){
        final String CLIENT_URL = "market://details?id=";
        final String BROWSER_URL = "https://play.google.com/store/apps/details?id=";

        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CLIENT_URL + context.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BROWSER_URL  + context.getPackageName())));
        }
    }
}
