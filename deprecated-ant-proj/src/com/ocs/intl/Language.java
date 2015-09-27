/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.intl;

/**
 *
 * @author yc06x
 */
public class Language {

    private int id;
    private String name;
    private String shortName;
    private boolean isDefault;

    public Language(int id, String name, String shortName, boolean isDefault) {
        this.id = id;
        this.name = name.toLowerCase();
        this.shortName = shortName.toLowerCase();
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public boolean isDefault() {
        return this.isDefault;
    }
    
}
