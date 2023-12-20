package com.chen.zhou.liang.lancet.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    public static String getTimeAsText(LocalDateTime dateTime) {
        return dateTimeFormatter.format(dateTime);
    }

    public static String getCurrentTimeAsText() {
        return dateTimeFormatter.format(LocalDateTime.now());
    }

}
