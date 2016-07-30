package com.example.monitor.utils;

/**
 * Содержит вспомогательные методы для логгирования
 */
public class LogUtils {

    private static final String PREFIX = "_";

    /**
     * Возвращает имя класса с префиксом
     */
    public static String makeLogTag(Class className){
        return PREFIX + className.getSimpleName();
    }
}
