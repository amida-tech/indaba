/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author Jeff
 */
public class QuestionOption {
    private int id;
    private String label = null;
    private String criteria = null;
    private String hint = null;
    private int weight;
    private int mask;
    private double score;
    private boolean useScore;
    public QuestionOption() {
        
    }
    public QuestionOption(int id, String label, String criteria, double score, boolean useScore, String hint, int mask) {
        this.id = id;
        this.label = label;
        this.criteria = criteria;
        this.score = score;
        this.useScore = useScore;
        this.hint = hint;
        this.mask = mask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
    
    public boolean hasUseScore() {
        return useScore;
    }

    public void setUseScore(boolean useScore) {
        this.useScore = useScore;
    }

    @Override
    public String toString() {
        return "QuestionOption{" + "id=" + id + ", label=" + label + ", criteria=" + criteria + ", hint=" + hint + ", weight=" + weight + ", mask=" + mask + ", score=" + score + '}';
    }
    
}
