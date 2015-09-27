/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class SurveyAnswerObject {

    private short answerType;
    private Object value;
    private int id;
    private int indicatorId;

    public void setAnswerType(short type) {
        this.answerType = type;
    }

    public short getAnswerType() {
        return this.answerType;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setIndicatorId(int id) {
        this.indicatorId = id;
    }

    public int getIndicatorId() {
        return this.indicatorId;
    }

}
