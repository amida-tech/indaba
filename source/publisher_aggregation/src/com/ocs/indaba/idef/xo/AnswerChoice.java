/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

/**
 *
 * @author yc06x
 */
public class AnswerChoice {

    private String option;
    private double score;
    private String criteria = null;
    private boolean hasScore = false;

    private int dboId;
    private int mask;
    private int weight;

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return this.mask;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return this.option;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return this.criteria;
    }

    public void setScore(double score) {
        this.score = score;
        this.hasScore = true;
    }

    public Double getScore() {
        if (this.hasScore) return score;
        else return null;
    }
}
