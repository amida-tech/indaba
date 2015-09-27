/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lukeshi
 */
public class ResultSetUtil {

    public static boolean getBoolean(ResultSet rs, String column, boolean defaultB) {
        boolean v = false;
        try {
            v = rs.getBoolean(column);
        } catch (SQLException ex) {
            v = defaultB;
        }
        return v;
    }

    public static boolean getBoolean(ResultSet rs, String column) {
        boolean v = false;
        try {
            v = rs.getBoolean(column);
        } catch (SQLException ex) {
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
        }
        return v;
    }

    public static int getInt(ResultSet rs, String column) {
        int v = -1;
        try {
            v = rs.getInt(column);
        } catch (SQLException ex) {
        }
        return v;
    }

    public static int getInt(ResultSet rs, String column, int defaultV) {
        int v = -1;
        try {
            v = rs.getInt(column);
        } catch (SQLException ex) {
            v = defaultV;
        }
        return v;
    }

    public static float getFloat(ResultSet rs, String column) {
        float v = -1;
        try {
            v = rs.getFloat(column);
        } catch (SQLException ex) {
        }
        return v;
    }

    public static float getFloat(ResultSet rs, String column, int defaultV) {
        float v = -1;
        try {
            v = rs.getFloat(column);
        } catch (SQLException ex) {
            v = defaultV;
        }
        return v;
    }

    public static long getLong(ResultSet rs, String column) {
        long v = -1L;
        try {
            v = rs.getLong(column);
        } catch (SQLException ex) {
        }
        return v;
    }

    public static long getLong(ResultSet rs, String column, long defaultV) {
        long v = -1;
        try {
            v = rs.getLong(column);
        } catch (SQLException ex) {
            v = defaultV;
        }
        return v;
    }

    public static String getString(ResultSet rs, String column) {
        String s = null;
        try {
            s = rs.getString(column);
        } catch (SQLException ex) {
        }
        return s;
    }

    public static String getString(ResultSet rs, String column, String defaultStr) {
        String s = null;
        try {
            s = rs.getString(column);
        } catch (SQLException ex) {
            s = defaultStr;
        }
        return s;
    }

    public static Date getDate(ResultSet rs, String column, Date defaultDate) {
        Date d = null;
        try {
            d = rs.getDate(column);
        } catch (SQLException ex) {
            d = defaultDate;
        }
        return d;
    }

    public static Date getDate(ResultSet rs, String column) {
        Date d = null;
        try {
            d = rs.getDate(column);
        } catch (SQLException ex) {
        }
        return d;
    }
}
