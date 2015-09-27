/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.util.ValueObject;

/**
 *
 * @author Jeff Jiang
 */
public class AnswerValueSettingVO extends ValueObject {

    private int id;
    private float minVal;
    private float maxVal;
    private float defaultVal;
    private String criteria;

    public AnswerValueSettingVO() {
    }

    public AnswerValueSettingVO(int id, float minVal, float maxVal, float defaultVal, String criteria) {
        this.id = id;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.defaultVal = defaultVal;
        this.criteria = (criteria == null) ? "" : criteria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
    public void initializeObject(Map<String, String> map) throws ReflectionException {
    this.setCriteria(map.get("criteria"));
    }
     */
    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = (criteria == null) ? "" : criteria;
    }

    public float getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(float defaultVal) {
        this.defaultVal = defaultVal;
    }

    public int getIntDefaultVal() {
        return (int) defaultVal;
    }

    public int getIntMaxVal() {
        return (int) maxVal;
    }

    public int getIntMinVal() {
        return (int) minVal;
    }

    public float getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(float maxVal) {
        this.maxVal = maxVal;
    }

    public float getMinVal() {
        return minVal;
    }

    public void setMinVal(float minVal) {
        this.minVal = minVal;
    }

    @Override
    public String toString() {
        return "AnswerValueSettingVO{" + "minVal=" + minVal + ", maxVal=" + maxVal + ", defaultVal=" + defaultVal + ", criteria=" + criteria + '}';
    }
}
