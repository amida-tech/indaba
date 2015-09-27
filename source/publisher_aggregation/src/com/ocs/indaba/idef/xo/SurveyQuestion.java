/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

/**
 *
 * @author yc06x
 */
public class SurveyQuestion {

    private String id;
    private String label;
    private int weight;

    private SurveyConfig surveyConfig;
    private Indicator indicator;
    private SurveyCategory parent;

    private int dboId;

    private Scorecard card;   // used for finding missing answers

    public void setSurveyConfig(SurveyConfig sc) {
        this.surveyConfig = sc;
    }

    public SurveyConfig getSurveyConfig() {
        return this.surveyConfig;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public Indicator getIndicator() {
        return this.indicator;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public void setParent(SurveyCategory parent) {
        this.parent = parent;
        parent.addQuestion(this);
    }

    public SurveyCategory getParent() {
        return parent;
    }

    public void setWeight(int w) {
        this.weight = w;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }

    public void setScorecard(Scorecard card) {
        this.card = card;
    }

    public Scorecard getScorecard() {
        return card;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
