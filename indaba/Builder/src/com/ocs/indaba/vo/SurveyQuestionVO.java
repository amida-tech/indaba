/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyQuestion;

/**
 *
 * @author Jeff
 */
public class SurveyQuestionVO {

    SurveyQuestion question;
    SurveyIndicator indicator;
    SurveyAnswer answer;
    boolean completed = false;
    private int reviewOption = -1;

    /**
     * Get the value of reviewOption
     *
     * @return the value of reviewOption
     */
    public int getReviewOption() {
        return reviewOption;
    }

    /**
     * Set the value of reviewOption
     *
     * @param reviewOption new value of reviewOption
     */
    public void setReviewOption(int reviewOption) {
        this.reviewOption = reviewOption;
    }


    public SurveyQuestionVO() {
    }

    public SurveyQuestionVO(SurveyQuestion question, SurveyIndicator indicator, SurveyAnswer answer) {
        this.question = question;
        this.indicator = indicator;
        this.answer = answer;
    }

    public SurveyAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(SurveyAnswer answer) {
        this.answer = answer;
    }

    public SurveyIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(SurveyIndicator indicator) {
        this.indicator = indicator;
    }

    public SurveyQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SurveyQuestion question) {
        this.question = question;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "SurveyQuestionVO{" + "completed=" + completed + "reviewOption=" + reviewOption + '}';
    }
    
}
