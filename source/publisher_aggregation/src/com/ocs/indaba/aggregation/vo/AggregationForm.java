/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import java.util.List;

/**
 *
 * @author luwb
 */
public class AggregationForm {
    private int id;
    private String name;
    private String shortName;
    private String description;
    private int worksetId;
    private String worksetName;
    private int holePolicy;
    private AggMethodVO aggMethod = null;
    private List<AggIndicatorVO> aggIndicators = null;
    private boolean changed = false;

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
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
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
     * @return the worksetId
     */
    public int getWorksetId() {
        return worksetId;
    }

    /**
     * @param worksetId the worksetId to set
     */
    public void setWorksetId(int worksetId) {
        this.worksetId = worksetId;
    }

    /**
     * @return the worksetName
     */
    public String getWorksetName() {
        return worksetName;
    }

    /**
     * @param worksetName the worksetName to set
     */
    public void setWorksetName(String worksetName) {
        this.worksetName = worksetName;
    }

    /**
     * @return the holePolicy
     */
    public int getHolePolicy() {
        return holePolicy;
    }

    /**
     * @param holePolicy the holePolicy to set
     */
    public void setHolePolicy(int holePolicy) {
        this.holePolicy = holePolicy;
    }

    /**
     * @return the aggMethod
     */
    public AggMethodVO getAggMethod() {
        return aggMethod;
    }

    /**
     * @param aggMethod the aggMethod to set
     */
    public void setAggMethod(AggMethodVO aggMethod) {
        this.aggMethod = aggMethod;
    }

    /**
     * @return the aggIndicators
     */
    public List<AggIndicatorVO> getAggIndicators() {
        return aggIndicators;
    }

    /**
     * @param aggIndicators the aggIndicators to set
     */
    public void setAggIndicators(List<AggIndicatorVO> aggIndicators) {
        this.aggIndicators = aggIndicators;
    }

    /**
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

}
