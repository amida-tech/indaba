/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class SurveyAnswerStatus {

    static public final short ANSWER_STATUS_COMPLETE   = 1;
    static public final short ANSWER_STATUS_INCOMPLETE = 2;
    static public final short ANSWER_STATUS_NOT_WROKED = 3;

    static public final short ANSWER_STATUS_AGREE = 11;
    static public final short ANSWER_STATUS_AGREE_W_RESERVATION = 12;
    static public final short ANSWER_STATUS_DISAGREE = 13;
    static public final short ANSWER_STATUS_NOT_QUALIFIED = 14;

    static public final short ANSWER_STATUS_NOT_TO_BE_WORKED = 21;

    private int index;
    private String questionText;
    private short status;
    private int flagCount;
    private String icon;
    private short answerType;

    public void setIndex(int value) {
        this.index = value;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIcon(String value) {
        this.icon = value;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setQuestionText(String value) {
        this.questionText = value;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public void setStatus(short value) {
        this.status = value;
    }

    public short getStatus() {
        return this.status;
    }

    public void setFlagCount(int value) {
        this.flagCount = value;
    }

    public int getFlagCount() {
        return this.flagCount;
    }

    public void setAnswerType(short value) {
        this.answerType = value;
    }

    public short getAnswerType() {
        return this.answerType;
    }

}
