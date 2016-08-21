package com.example.monitor.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Занимается конвертацией одних объектов в другие.
 */
public class ConvertUtils {

    public static String formatTime(Float source){
       int hours = (int) (source / 3600);
       int minutes = (int) (source % 3600) / 60;
       int seconds = (int) (source % 60);
       if (hours > 1){
           String formattedDate = "" + hours + "h " + minutes + "m";
           return formattedDate;
       }
       String formattedDate = "" + minutes + "m";
       return formattedDate;
    }

    public static String secondToDate(long second) {
        Date date = new Date(second * 1000L); // *1000 is to convert seconds to milliseconds
        DateFormat df = SimpleDateFormat.getDateInstance();
        return df.format(date);
    }
}
