/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

/**
 *
 * @author yc06x
 */
public class QuestionAspect {

    int qstId;
    String qstName;
    String qstPublicName;
    String qstText;
    int answerType;
    String choiceLabel;
    Double score;
    String criteria;

    public void setQuestionId(int v) {
        this.qstId = v;
    }

    public int getQuestionId() {
        return this.qstId;
    }

    public void setQuestionName(String v) {
        this.qstName = v;
    }

    public String getQuestionName() {
        return this.qstName;
    }

    public void setQuestionPublicName(String v) {
        this.qstPublicName = v;
    }

    public String getQuestionPublicName() {
        return this.qstPublicName;
    }

    public void setQuestionText(String v) {
        this.qstText = v;
    }

    public String getQuestionText() {
        return this.qstText;
    }

    public void setAnswerType(int v) {
        this.answerType = v;
    }

    public int getAnswerType() {
        return this.answerType;
    }

    public void setChoiceLabel(String v) {
        this.choiceLabel = v;
    }

    public String getChoiceLabel() {
        return this.choiceLabel;
    }

    public void setCriteria(String v) {
        this.criteria = v;
    }

    public String getCriteria() {
        return this.criteria;
    }

    public void setScore(Double v) {
        this.score = v;
    }

    public Double getScore() {
        return score;
    }

}
