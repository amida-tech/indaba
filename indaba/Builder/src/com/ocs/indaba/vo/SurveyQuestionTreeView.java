/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.SurveyQuestion;


/**
 *
 * Yan Cheng
 */
public class SurveyQuestionTreeView extends SurveyQuestion {

    private String text;
    private short answerType;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setAnswerType(short value) {
        this.answerType = value;
    }

    public short getAnswerType() {
        return this.answerType;
    }

}
