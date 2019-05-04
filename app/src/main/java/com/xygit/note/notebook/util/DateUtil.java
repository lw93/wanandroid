package com.xygit.note.notebook.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Created by xiuyaun
 * @time on 2019/4/14
 */

public class DateUtil {
    private static final String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
    private static final String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY);
        }
    };

    public static String strToDate(long time) {
        DateFormat dateFormat = df.get();
        return dateFormat.format(new Date(time));
    }

    public static String strToDate(Date time) {
        DateFormat dateFormat = df.get();
        return dateFormat.format(time);
    }


    public static long strToDay(String time) {
        DateFormat dateFormat = df.get();
        try {
            return dateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
