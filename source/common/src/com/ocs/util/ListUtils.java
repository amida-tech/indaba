/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jeff
 */
public class ListUtils {

    public static boolean isEmptyList(List list) {
        return ((list == null) || list.isEmpty());
    }

    public static <T> String listToString(List<T> list) {
        return listToString(list, ",");
    }

    public static <T> String listToString(List<T> list, String separator) {
        if(list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) == null) {
                continue;
            }
            sb.append(list.get(i).toString());
            if (i != (list.size() - 1)) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static List<Integer> strArrToList(String[] arr) {
        List<Integer> list = new ArrayList<Integer>();
        if(arr!= null && arr.length>0){
            for(String s: arr) {
                try{
                    list.add(Integer.parseInt(s));
                } catch(Exception ex) {
                }
            }
        }
        return list;
    }
    /*get the intersection of two list*/
    public static <T> List<T> getIntersection(List<T> firstList, List<T> lastList) {
        Set<T> set = new HashSet<T>();
        set.addAll(firstList);
        set.retainAll(lastList);
        return new ArrayList(set);
    }
}
