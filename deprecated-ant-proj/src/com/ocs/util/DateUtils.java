/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author jiangjeff
 */
public class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_1 = "hh:mm a MMMMM dd, yyyy";
    public static final String DATE_FORMAT_2 = "MMM d, yyyy HH:mm:ss";
    public static final String DATE_FORMAT_3 = "MMM d, yyyy";
    public static final String DATE_FORMAT_4 = "hh:mm aa";
    public static final String DATE_FORMAT_5 = "MMM d, yyyy HH:mm aa";
    public static final String DATE_FORMAT_6 = "MM/dd/yyyy";
    public static final String DATE_FORMAT_7 = "yyyy-MM-dd h:mm a";

    public static final String format(Date date, String format, Locale locale) {
        return (date == null) ? "" : new SimpleDateFormat(format, locale).format(date);
    }

    public static final String format(Date date, String format) {
        return (date == null) ? "" : new SimpleDateFormat(format).format(date);
    }

    public static final String format(Date date) {
        return (date == null) ? "" : DateUtils.format(date, DEFAULT_DATE_FORMAT);
    }

    public static final Date parse(String timeStr, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(timeStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static final Date parse(String timeStr, String format, Locale locale) {
        DateFormat df = new SimpleDateFormat(format, locale);
        Date date = null;
        try {
            date = df.parse(timeStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static final Date[] getOneWeekSpan() {
        Date[] dates = new Date[2];
        Calendar cal = Calendar.getInstance();
        dates[1] = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        dates[0] = cal.getTime();
        return dates;
    }

    public static final Date[] getTwoWeekSpan() {
        Date[] dates = new Date[2];
        Calendar cal = Calendar.getInstance();
        dates[1] = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -14);
        dates[0] = cal.getTime();
        return dates;
    }

    public static final Date[] getOneMonthSpan() {
        Date[] dates = new Date[2];
        Calendar cal = Calendar.getInstance();
        dates[1] = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        dates[0] = cal.getTime();
        return dates;
    }

    public static final Date[] getThreeMonthSpan() {
        Date[] dates = new Date[2];
        Calendar cal = Calendar.getInstance();
        dates[1] = cal.getTime();
        cal.add(Calendar.MONTH, -3);
        dates[0] = cal.getTime();
        return dates;
    }

    public static Date today() {
        Calendar cal = Calendar.getInstance();
        return DateUtils.justDate(cal.getTime());
    }

    public static Date tomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return DateUtils.justDate(cal.getTime());
    }

    public static Date justDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = df.format(date);
        date = null;
        try {
            date = df.parse(todayStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static Date[] getDays(int n) {
        Date[] dates = new Date[2];
        // dates[1] = new Date();  // DateUtils.tomorrow();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 11);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.AM_PM, Calendar.PM);
        dates[1] = cal.getTime();

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -n);
        dates[0] = DateUtils.justDate(cal.getTime());
        return dates;
    }

    public static Date[] getWeeks(int n) {
        Date[] dates = new Date[2];
        dates[1] = new Date();  // DateUtils.tomorrow();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7 * n);
        dates[0] = DateUtils.justDate(cal.getTime());
        return dates;
    }

    public static Date[] getMonths(int n) {
        Date[] dates = new Date[2];
        dates[1] = new Date();  // DateUtils.tomorrow();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -n);
        dates[0] = DateUtils.justDate(cal.getTime());
        return dates;
    }

    public static Date[] getYears(int n) {
        Date[] dates = new Date[2];
        dates[1] = new Date();  // DateUtils.tomorrow();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -n);
        dates[0] = DateUtils.justDate(cal.getTime());
        return dates;
    }

    public static int getDaysDiff(Date date1, Date date2) {
        long day = 0;
        try {
            day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
        }
        return (int) day;
    }

    public static long getUtcInMillis(String tzName, Date time) {
        TimeZone tz = TimeZone.getTimeZone(tzName);

        if (tz == null) return -1;   // bad timezone;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(time);
        int year = c1.get(Calendar.YEAR);
        int month = c1.get(Calendar.MONTH);
        int day = c1.get(Calendar.DAY_OF_MONTH);
        int hour = c1.get(Calendar.HOUR_OF_DAY);
        int minute = c1.get(Calendar.MINUTE);
        int sec = c1.get(Calendar.SECOND);

        Calendar c2 = Calendar.getInstance(tz);
        c2.set(year, month, day, hour, minute, sec);

        return c2.getTimeInMillis();
    }


    public static void main(String[] args) {

        String[] allTimeZones = TimeZone.getAvailableIDs();

        Arrays.sort(allTimeZones);

        for (String timezone : allTimeZones) {
            System.out.println(timezone);
        }
    }
}
