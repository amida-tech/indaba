/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.util;

import com.ocs.indaba.common.Constants;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jeff
 */
public class DateUtils {

    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_SIMPLE_FORMAT = "yyyy-MM-dd HHmmss";
    public static final String DEFAULT_DATE_SIMPLE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT_1 = "yy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT_2 = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT_3 = "yyMMddHHmmssS";
    public static final int MILLISECONDS_OF_DAY = 1000 * 3600 * 24;
    public static final int MILLISECONDS_OF_MINUTE = 1000 * 60;

    public static final Date str2Date(String s) {
        return str2Date(s, DEFAULT_DATETIME_FORMAT);
    }

    public static final Date str2Date(String s, String fmt) {
        DateFormat df = new SimpleDateFormat(fmt);
        Date date = null;
        try {
            date = df.parse(s);
        } catch (ParseException ex) {
        }
        return date;
    }

    public static final String date2Str(Date d, String fmt) {
        if (d == null)
            return "";
        DateFormat df = new SimpleDateFormat(fmt);
        return df.format(d);
    }

    public static final String date2Str(Date d) {
        return date2Str(d, DEFAULT_DATE_FORMAT);
    }

    public static final String datetime2Str(Date d) {
        return date2Str(d, DEFAULT_DATETIME_FORMAT);
    }

    public static final String date2SimpleStr(Date d) {
        return date2Str(d, DEFAULT_DATE_SIMPLE_FORMAT);
    }

    public static final String datetime2SimpleStr(Date d) {
        return date2Str(d, DEFAULT_DATETIME_SIMPLE_FORMAT);
    }

    public static long getIntervalInMilliSeconds(Date begin, Date end) {
        // Fix NPE in case begin is null or end is null if any
        if (begin == null && end != null) {
            begin = end;
        } else if (begin != null && end == null) {
            end = begin;
        } else if (begin == null && end == null) {
            begin = new Date();
            end = begin;
        }
        
        long interval = end.getTime() - begin.getTime();
        if (interval == 0) {
            return 0;
        } else if (interval < 0) {
            interval = -interval;
        }
        return interval;
    }

    public static int getIntervalInDays(Date begin, Date end) {
        return milliSecondsToDays(getIntervalInMilliSeconds(begin, end));
    }

    public static int getIntervalInMinutes(Date begin, Date end) {
        return milliSecondsToMinutes(getIntervalInMilliSeconds(begin, end));
    }

    public static int milliSecondsToDays(long interval) {
        return (int) (interval / MILLISECONDS_OF_DAY);
        //float f = (float) interval / MILLISECONDS_OF_DAY;
        //return java.lang.Math.round(f);
    }

    public static int milliSecondsToMinutes(long interval) {
        return (int) (interval / MILLISECONDS_OF_MINUTE);
    }

    public static Date nextIntervalDays(Date date, int intervalDays) {
        if(date == null) {
            date = new Date();
        }
        date.setTime(date.getTime() + intervalDays * Constants.MILLSECONDS_PER_DAY);
        return date;
    }

    public static int getDaysFrom(Date begin) {
        return getIntervalInDays(begin, new Date());
    }

    public static int getDaysTo(Date end) {
        return getIntervalInDays(new Date(), end);
    }

    public static int getMinutesFrom(Date begin) {
        return getIntervalInMinutes(begin, new Date());
    }
}
