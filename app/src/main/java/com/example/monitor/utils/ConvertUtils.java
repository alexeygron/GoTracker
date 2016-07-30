package com.example.monitor.utils;

/**
 * Занимается конвертацией одних объектов в другие.
 */
public class ConvertUtils {

   static public String formatTime(Float source){
       int hours = (int) (source / 3600);
       int minutes = (int) (source % 3600) / 60;
       int seconds = (int) (source % 60);

       if (hours > 1){
           String formattedDate = "" + hours + "h " + minutes + "m " + seconds + "s ";
           return formattedDate;
       }
       String formattedDate = "" + minutes + "m " + seconds + "s ";
       return formattedDate;
    }
}
