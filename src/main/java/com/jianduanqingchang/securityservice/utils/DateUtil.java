package com.jianduanqingchang.securityservice.utils;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * @author yujie
 */
@Log4j2
public class DateUtil {

    private DateUtil() {
    }

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Check if the current time is before the param data.
     * @param date date
     * @return true if current time if bigger than the param data
     */
    public static boolean isBeforeNow(String date) {
        return DateUtil.compareDate(DateUtil.getTime(), date);
    }
    
    /**
     * Get current time in YYYY-MM-DD HH:mm:ss format string
     *
     * @return Current time in HH:mm:ss format string
     */
    public static String getTime() {
        var sdfTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        return sdfTimeFormat.format(new Date());
    }
    
    /**
     * Date String compare
     * if s>=e return true else false
     *
     * @param s date 1
     * @param e date 2
     * @return s>=e
     */
    public static boolean compareDate(String s, String e) {
        if (formatDate(s) == null || formatDate(e) == null) {
            return false;
        }
        return s.compareTo(e) > 0;
    }
    
    /**
     * Format date string info date type
     *
     * @param date String in yyyy-MM-dd format
     * @return Date type in yyyy-MM-dd
     */
    public static Date formatDate(String date) {
        var sdfDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdfDateFormat.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Format date string info date type
     *
     * @param time String in yyyy-MM-dd HH:mm:ss format
     * @return Date type in yyyy-MM-dd HH:mm:ss format
     */
    public static Date formatTime(String time) {
        var sdfTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        try {
            return sdfTimeFormat.parse(time);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Change Date into Timestamp
     *
     * @param date Date
     * @return Timestamp,yyyy-MM-dd HH:mm:ss
     */
    public static Timestamp formatTime(@NonNull Date date){
        var sdfTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        return new Timestamp(Objects.requireNonNull(formatTime(sdfTimeFormat.format(date))).getTime());
    }

    /**
     * Change Timestamp into Date
     *
     * @param timestamp timestamp
     * @return Date,yyyy-MM-dd HH:mm:ss
     */
    public static Date formatTime(Timestamp timestamp) {
        var sdfTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        return formatTime(sdfTimeFormat.format(timestamp));
    }
    
    /**
     * Get the corresponding day after i days from today
     *
     * @param day i
     * @return date string
     */
    public static String getAddDayDate(int day) {
        var sdfDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String currentTime = DateUtil.getTime();
        var gCal = new GregorianCalendar(
                Integer.parseInt(currentTime.substring(0, 4)),
                Integer.parseInt(currentTime.substring(5, 7)) - 1,
                Integer.parseInt(currentTime.substring(8, 10)));
        gCal.add(Calendar.DATE, day);
        return sdfDateFormat.format(gCal.getTime());
    }
    
    /**
     * Get the corresponding time after i days from now
     * Precise to second
     *
     * @param day i
     * @return date string
     */
    public static String getAddDayTime(long day) {
        var sdfTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        var date = new Date(System.currentTimeMillis() +  day * 24 * 60 * 60 * 1000L);
        return sdfTimeFormat.format(date);
    }
    
    /**
     * Get the corresponding time after i seconds
     * Precise to second
     *
     * @param sec i
     * @return date string
     */
    public static String getAddSecondTime(long sec) {
        var sdfTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        var date = new Date(System.currentTimeMillis() + sec * 1000L);
        return sdfTimeFormat.format(date);
    }
}