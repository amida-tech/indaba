/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

/**
 *
 * @author seanpcheng
 */
public class IndicatorChoice {
    private int id;
    private String label = null;
    private double score = 0;
    private boolean useScore = false;
    private String criteria = null;
    private int atcChoiceId = -1;
    
    public IndicatorChoice() {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    //Setters
    public void setLabel(String label) {
        this.label = label;
    }
    
    public void setScore(double score) {
        this.score = score;
    }
    
    public void setUseScore(boolean useScore) {
        this.useScore = useScore;
    }
    
    public void setCriteria(String criteria) {
        this.criteria = (criteria == null) ? "" : criteria;
    }

    public void setAtcChoiceId(int id) {
        this.atcChoiceId = id;
    }
    
    //Getters
    public String getLabel() {
        return this.label;
    }
    
    public double getScore() {
        return this.score;
    }
    
    public boolean getUseScore() {
        return this.useScore;
    }
    
    public String getCriteria() {
        return (criteria == null) ? "" : criteria;
    }

    public int getAtcChoiceId() {
        return this.atcChoiceId;
    }
}
