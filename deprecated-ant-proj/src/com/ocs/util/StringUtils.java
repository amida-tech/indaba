/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.util;

import java.util.ArrayList;
import java.util.List;

/**
 * String utilities.
 * 
 * @author Jeff
 * 
 */
public final class StringUtils extends org.apache.commons.lang.StringUtils {

    /**
     * Convert string to integer
     * @param s
     * @return the integer value. Note, Integer.MIN_VALUE will be returned if the specified string is not a valid integer.
     */
    public static byte str2byte(String s) {
        byte v = Byte.MIN_VALUE;
        try {
            v = Byte.parseByte(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to integer
     * @param s
     * @return the integer value. Note, defaultValue will be returned if the specified string is not a valid integer.
     */
    public static byte str2byte(String s, byte defaultValue) {
        byte v = defaultValue;
        try {
            v = Byte.parseByte(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to integer
     * @param s
     * @return the integer value. Note, Integer.MIN_VALUE will be returned if the specified string is not a valid integer.
     */
    public static short str2short(String s) {
        short v = Short.MIN_VALUE;
        try {
            v = Short.parseShort(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to integer
     * @param s
     * @return the integer value. Note, defaultValue will be returned if the specified string is not a valid integer.
     */
    public static short str2short(String s, short defaultValue) {
        short v = defaultValue;
        try {
            v = Short.parseShort(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to integer
     * @param s
     * @return the integer value. Note, Integer.MIN_VALUE will be returned if the specified string is not a valid integer.
     */
    public static int str2int(String s) {
        int v = Integer.MIN_VALUE;
        try {
            v = Integer.parseInt(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to integer
     * @param s
     * @return the integer value. Note, defaultValue will be returned if the specified string is not a valid integer.
     */
    public static int str2int(String s, int defaultValue) {
        int v = defaultValue;
        try {
            v = Integer.parseInt(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to integer
     * @param s
     * @return the integer value. Note, Integer.MIN_VALUE will be returned if the specified string is not a valid integer.
     */
    public static long str2long(String s) {
        long v = Integer.MIN_VALUE;
        try {
            v = Long.parseLong(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to integer
     * @param s
     * @return the integer value. Note, defaultValue will be returned if the specified string is not a valid integer.
     */
    public static long str2long(String s, long defaultValue) {
        long v = defaultValue;
        try {
            v = Long.parseLong(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to boolean
     * @param s
     * @return the integer value. Note, false will be returned if the specified string is not a valid integer.
     */
    public static boolean str2boolean(String s) {
        boolean v = false;
        try {
            if (s != null) s = s.trim();
            if ("1".equals(s)) {
                v = true;
            } else if ("0".equals(s)) {
                v = false;
            } else {
                v = Boolean.parseBoolean(s);
            }
        } catch (Exception ex) {
        }
        return v;
    }

    /**
     * Convert string to boolean
     * @param s
     * @return the integer value. Note, defaultValue will be returned if the specified string is not a valid integer.
     */
    public static boolean str2boolean(String s, boolean defaultValue) {
        boolean v = defaultValue;
        try {
            v = Boolean.parseBoolean(s.trim());
        } catch (Exception ex) {
        }
        return v;
    }

    public static float str2float(String s, float defaultValue) {
        float v = 0f;
        try {
            v = Float.parseFloat(s.trim());
        } catch (Exception ex) {
            return defaultValue;
        }
        return v;
    }

    public static float str2float(String s) {
        return str2float(s, -1);
    }

    public static double str2double(String s, double defaultValue) {
        double v = 0f;
        try {
            v = Double.parseDouble(s.trim());
        } catch (Exception ex) {
            return defaultValue;
        }
        return v;
    }

    public static double str2double(String s) {
        return str2double(s, -1);
    }

    /**
     * Convert string array to integer array
     * @param s
     * @return the integer value. Note, defaultValue will be returned if the specified string is not a valid integer.
     */
    public static List<Integer> strArr2IntList(String[] sArr) {
        if (sArr == null || sArr.length == 0) {
            return null;
        }
        List<Integer> list = new ArrayList<Integer>();
        for (String s : sArr) {
            if (s == null || (s = s.trim()).length() == 0) {
                continue;
            }
            try {
                list.add(Integer.parseInt(s.trim()));
            } catch (Exception ex) {
            }
        }
        return list;
    }

    public static long wordcount(String line) {
        String strBuf = line.replaceAll("\\<.*?>", "");
        strBuf = strBuf.replace("&nbsp;", " ");
        strBuf = strBuf.replace("&quot;", " ");
        strBuf = strBuf.replace("&rsquo;", " ");
        String[] results = strBuf.split("\\W+");
        return (results != null) ? results.length : 0;
    }

    public static long wordcount2(String line) {
        long numWords = 0;
        int index = 0;
        boolean prevWhiteSpace = true;
        while (index < line.length()) {
            char c = line.charAt(index++);
            boolean currWhiteSpace = !Character.isLetterOrDigit(c);
            if (prevWhiteSpace && !currWhiteSpace) {
                numWords++;
            }
            prevWhiteSpace = currWhiteSpace;
        }
        return numWords;
    }

    //
    public static String encode(int i) {
        if (i == 0) {
            return "";
        }
        return "g" + i;
    }

    public static String encode(Integer i) {
        if (i == 0) {
            return "";
        }
        return "g" + i;
    }

    public static int decode(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        return Integer.parseInt(s.substring(1));
    }

    public static String escapeXML(String str) {
        if (isEmpty(str)) {
            return str;
        }
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("'", "&apos;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }

    public static boolean containsIgnoreCase(String parent, String subStr) {
        if (isEmpty(parent) || isEmpty(subStr)) {
            return false;
        }
        if (parent.toLowerCase().indexOf(subStr.toLowerCase()) >= 0) {
            return true;
        }
        return false;
    }

    public static String list2Str(List list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : list) {
            if (obj != null) {
                sb.append(obj.toString()).append(",");
            }
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String list2Str(List list, String separator) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : list) {
            if (obj != null) {
                sb.append(obj.toString()).append(separator);
            }
        }
        sb.setLength(sb.length() - separator.length());
        return sb.toString();
    }

    public static String list2Str(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr) {
            if (obj != null) {
                sb.append(obj.toString()).append(",");
            }
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String list2Str(Object[] arr, String separator) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr) {
            if (obj != null) {
                sb.append(obj.toString()).append(separator);
            }
        }
        sb.setLength(sb.length() - separator.length());
        return sb.toString();
    }

    public static String null2empty(String str) {
        if (str != null) {
            return str;
        }
        return "";
    }

    public static String normalize(String str) {
        String result = str.replaceAll("\\s+", " ");
        return result.trim();
    }

    public static String replace(String str, String target, String replacement) {
        return isEmpty(str) ? "" : str.replace(target, replacement);
    }

    public static String replaceAll(String str, String regex, String replacement) {
        return isEmpty(str) ? "" : str.replaceAll(regex, replacement);
    }

    public static void main(String args[]) {
        System.out.println(replaceAll("http://localhost:8080/indaba/surveyPreVersionDisplay.do?contentVersionId=4&action=display&horseid=8", "contentVersionId=[\\d]+", "contentVersionId=6"));
    }
}
