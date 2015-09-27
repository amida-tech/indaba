/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author luwb
 */
public class ListUtils {

    public static boolean isEmptyList(List list) {
        return ((list == null) || list.isEmpty());
    }

    public static <T> String listToString(List<T> list) {
        if (list == null) return null;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toString());
            if (i != (list.size() - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /*get the intersection of two list*/
    public static <T> List<T> getIntersection(List<T> firstList, List<T> lastList) {
        Set<T> set = new HashSet<T>();
        set.addAll(firstList);
        set.retainAll(lastList);
        return new ArrayList(set);
    }
}
