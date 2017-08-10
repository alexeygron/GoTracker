package com.example.monitor.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Containg different auxiliary methods
 */
public class Helpers {

    private static final String PREFIX = "_";

    public static String makeLogTag(Class className) {
        return PREFIX + className.getSimpleName();
    }

    /**
     * Check the network avaibled
     *
     * @return state
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    /**
     * Calls the application page in Google Play
     */
    public static void openPlayStorePage(Context context) {
        final String CLIENT_URL = "market://details?id=";
        final String BROWSER_URL = "https://play.google.com/store/apps/details?id=";

        // In Google Play or system default browser
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CLIENT_URL + context.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BROWSER_URL + context.getPackageName())));
        }
    }
}
