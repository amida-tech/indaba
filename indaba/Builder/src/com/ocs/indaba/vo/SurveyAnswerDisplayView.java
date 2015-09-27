/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import java.util.List;

/**
 *
 * @author luwb
 */
 public class SurveyAnswerDisplayView {
    private SurveyAnswerView surveyAnswerView;
    private int answerObjectId;
    private int referenceObjectId;
    private String comments;

    /**
     * @return the surveyAnswerView
     */
    public SurveyAnswerView getSurveyAnswerView() {
        return surveyAnswerView;
    }

    /**
     * @param surveyAnswerView the surveyAnswerView to set
     */
    public void setSurveyAnswerView(SurveyAnswerView surveyAnswerView) {
        this.surveyAnswerView = surveyAnswerView;
    }

    /**
     * @return the answerObjectId
     */
    public int getAnswerObjectId() {
        return answerObjectId;
    }

    /**
     * @param answerObjectId the answerObjectId to set
     */
    public void setAnswerObjectId(int answerObjectId) {
        this.answerObjectId = answerObjectId;
    }

    /**
     * @return the referenceObjectId
     */
    public int getReferenceObjectId() {
        return referenceObjectId;
    }

    /**
     * @param referenceObjectId the referenceObjectId to set
     */
    public void setReferenceObjectId(int referenceObjectId) {
        this.referenceObjectId = referenceObjectId;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

}
