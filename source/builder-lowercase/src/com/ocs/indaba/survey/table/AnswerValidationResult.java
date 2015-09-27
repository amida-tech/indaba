/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.survey.table;

import java.util.Map;

/**
 *
 * @author yc06x
 */
public class AnswerValidationResult {

    private String error;
    private String errorField;
    private Map<Integer, IndicatorInfo> indicatorMap;
    private int questionCount = 0;
    private int answerCount = 0;

    public void setError(String err) {
        this.error = err;
    }

    public String getError() {
        return this.error;
    }

    public void setErrorField(String fieldName) {
        this.errorField = fieldName;
    }

    public String getErrorField() {
        return this.errorField;
    }

    public void setIndicatorMap(Map<Integer, IndicatorInfo> map) {
        this.indicatorMap = map;
    }

    public Map<Integer, IndicatorInfo> getIndicatorMap() {
        return this.indicatorMap;
    }


    public void setQuestionCount(int value) {
        this.questionCount = value;
    }

    public int getQuestionCount() {
        return this.questionCount;
    }


    public void setAnswerCount(int value) {
        this.answerCount = value;
    }

    public int getAnswerCount() {
        return this.answerCount;
    }

}
