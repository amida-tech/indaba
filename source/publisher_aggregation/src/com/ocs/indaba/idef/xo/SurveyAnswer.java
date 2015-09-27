/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

import java.util.List;

/**
 *
 * @author yc06x
 */
public class SurveyAnswer {

    private String comments = null;
    private int refChoices = 0;
    private String refDesc = null;

    private Scorecard scorecard;
    private SurveyQuestion question;
    private Answer answer;
    private List<Review> reviews;

    private int dboId;
    private int dboRefObjId;

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }

    public void setRefChoices(int choices) {
        this.refChoices = choices;
    }

    public int getRefChoices() {
        return this.refChoices;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setRefDesc(String v) {
        this.refDesc = v;
    }

    public String getRefDesc() {
        return refDesc;
    }

    public void setScorecard(Scorecard v) {
        this.scorecard = v;
    }

    public Scorecard getScorecard() {
        return this.scorecard;
    }

    public void setQuestion(SurveyQuestion q) {
        this.question = q;
    }

    public SurveyQuestion getQuestion() {
        return this.question;
    }

    public void setAnswer(Answer v) {
        this.answer = v;
    }

    public Answer getAnswer() {
        return this.answer;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return this.reviews;
    }
}
