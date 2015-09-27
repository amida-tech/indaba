/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jeff Jiang
 */
public final class RegexUtil {

    private final static Map<String, Pattern> patternCache = new HashMap<String, Pattern>();

    public static String find(String s, String regex) {
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }
        Matcher matcher = pattern.matcher(s);
        return matcher.find()?matcher.group():null;
    }

    public static String findLast(String s, String regex) {
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }
        Matcher matcher = pattern.matcher(s);
        return matcher.find()?matcher.group(matcher.groupCount() - 1):null;
    }

    public static String find(String s, String regex, int index) {
      String[] matches = findAll(s, regex);
      return (matches == null || index > matches.length - 1)?null:matches[index];
    }

    public static String[] findAll(String s, String regex) {
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }
        Matcher matcher = pattern.matcher(s);
        if(!matcher.find()) {
            return null;
        }
        String[] matches = new String[matcher.groupCount()];
        for(int i = 0, count = matcher.groupCount(); i < count; ++i) {
            matches[i] = matcher.group(i);
        }
        return matches;
    }
    
    public static List<String[]> findAllArr(String s, String regex) {
		if (s == null || regex == null)
			return null;
		List<String[]> list = new ArrayList<String[]>();
		String[] matchedRslts = null;

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);

		while (matcher.find()) {
			matchedRslts = new String[matcher.groupCount() + 1];

			for (int i = 0, n = matchedRslts.length; i < n; ++i) {
				matchedRslts[i] = matcher.group(i);
			}
			list.add(matchedRslts);
		}

		return list;
	}
    
    public static String[] findAllArr(String s, String regex, int group) {
		List<String[]> list = findAllArr(s, regex);
		if (list == null || list.size() == 0 || list.get(0).length <= group)
			return null;
		String[] matches = new String[list.size()];
		int i = 0;
		for(String[] keys : list){
			matches[i] = keys[group];
			i++;
    	}
		return matches;
	}
}
