/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author luwb
 */
public class AggMethodVO {
    private int id;
    private String name;
    private String description;
    private int showWeight;//0 means no, 1 show

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the showWeight
     */
    public int getShowWeight() {
        return showWeight;
    }

    /**
     * @param showWeight the showWeight to set
     */
    public void setShowWeight(int showWeight) {
        this.showWeight = showWeight;
    }
}
