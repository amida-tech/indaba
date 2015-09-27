/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.common;

/**
 *
 * @author yc06x
 */
public class MappingRule {

    private String name;
    private String[] parms;


    public MappingRule(String name, String[] parms) {
        this.name = name;
        this.parms = parms;
    }

    // rule syntax:   name:parm,parm,parm

    public static MappingRule parseRule(String str) {
        if (str == null) return null;

        str = str.trim();
        String[] parts = str.split(":");
        if (parts.length == 1) {
            return new MappingRule(parts[0], null);
        } else if (parts.length != 2) {
            // bad
            return null;
        }

        String[] parms = parts[1].split(",");

        return new MappingRule(parts[0], parms);
    }


    public String getName() {
        return this.name;
    }

    public String[] getParms() {
        return parms;
    }


    public String evaluate(String oldValue) {
        if (name.equalsIgnoreCase("none")) return oldValue;

        if (name.equals("replacePrefix")) {
            if (parms.length < 1) return oldValue;

            int sep = parms[0].charAt(0);
            String newPrefix = parms.length > 1 ? parms[1] : "";
            int sepPos = oldValue.lastIndexOf(sep);
            if (sepPos < 0) return oldValue;
            String keep = oldValue.substring(sepPos+1);
            return newPrefix + keep;
        }

        return oldValue;
    }

}
