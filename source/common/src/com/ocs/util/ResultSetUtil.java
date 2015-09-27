/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author lukeshi
 */
public class ResultSetUtil {
    private static final Logger logger = Logger.getLogger(ResultSetUtil.class);
    public static boolean getBoolean(ResultSet rs, String column, boolean defaultB) {
        boolean v = false;
        try {
            v = rs.getBoolean(column);
        } catch (SQLException ex) {
            v = defaultB;
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static boolean getBoolean(ResultSet rs, String column) {
        boolean v = false;
        try {
            v = rs.getBoolean(column);
        } catch (SQLException ex) {
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static short getShort(ResultSet rs, String column) {
        short v = -1;
        try {
            v = rs.getShort(column);
        } catch (SQLException ex) {
        }
        return v;
    }

    public static short getShort(ResultSet rs, String column, short defaultV) {
        short v = -1;
        try {
            v = rs.getShort(column);
        } catch (SQLException ex) {
            v = defaultV;
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static int getInt(ResultSet rs, String column) {
        int v = -1;
        try {
            v = rs.getInt(column);
        } catch (SQLException ex) {
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static int getInt(ResultSet rs, String column, int defaultV) {
        int v = -1;
        try {
            v = rs.getInt(column);
        } catch (SQLException ex) {
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static float getFloat(ResultSet rs, String column) {
        float v = -1;
        try {
            v = rs.getFloat(column);
        } catch (SQLException ex) {
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static float getFloat(ResultSet rs, String column, int defaultV) {
        float v = -1;
        try {
            v = rs.getFloat(column);
        } catch (SQLException ex) {
            v = defaultV;
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static long getLong(ResultSet rs, String column) {
        long v = -1L;
        try {
            v = rs.getLong(column);
        } catch (SQLException ex) {
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static long getLong(ResultSet rs, String column, long defaultV) {
        long v = -1;
        try {
            v = rs.getLong(column);
        } catch (SQLException ex) {
            v = defaultV;
            logger.error("Get column error: " + column, ex);
        }
        return v;
    }

    public static String getString(ResultSet rs, String column) {
        String s = null;
        try {
            s = rs.getString(column);
        } catch (SQLException ex) {
            logger.error("Get column error: " + column, ex);
        }
        return s;
    }

    public static String getString(ResultSet rs, String column, String defaultStr) {
        String s = null;
        try {
            s = rs.getString(column);
        } catch (SQLException ex) {
            s = defaultStr;
            logger.error("Get column error: " + column, ex);
        }
        return s;
    }

    public static Date getDate(ResultSet rs, String column, Date defaultDate) {
        Date d = null;
        try {
            d = rs.getDate(column);
        } catch (SQLException ex) {
            d = defaultDate;
            logger.error("Get column error: " + column, ex);
        }
        return d;
    }

    public static Date getDate(ResultSet rs, String column) {
        Date d = null;
        try {
            d = rs.getDate(column);
        } catch (SQLException ex) {
            logger.error("Get column error: " + column, ex);
        }
        return d;
    }

    public static Time getTime(ResultSet rs, String column) {
        Time t = null;
        try {
            t = rs.getTime(column);
        } catch (SQLException ex) {
            logger.error("Get column error: " + column, ex);
        }
        return t;
    }
}
