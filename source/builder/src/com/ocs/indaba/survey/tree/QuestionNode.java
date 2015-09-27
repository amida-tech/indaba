/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.survey.tree;

import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyQuestion;

/**
 *
 * @author Jeff
 */
public class QuestionNode extends Node {

    public static final short ANSWER_STATUS_NONE = 0;
    public static final short ANSWER_STATUS_INCOMPLETE = 1;
    public static final short ANSWER_STATUS_COMPLETE = 2;


    private short status = ANSWER_STATUS_NONE;
    private int reviewOption = -1;
    private SurveyQuestion question = null;
    private SurveyIndicator indicator = null;
    private SurveyAnswer answer = null;
    private String publicName = null;
    private String text = null;
    private short answerType;

    public QuestionNode(int id, String publicName, String text, int parentId, int weight, short answerType) {
        super(Node.NODE_TYPE_QUESTION, id, (publicName + ". " + text), parentId, weight);
        this.publicName = publicName;
        this.text = text;
        this.answerType = answerType;
    }

    public SurveyAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(SurveyAnswer answer) {
        this.answer = answer;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
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

    public int getReviewOption() {
        return reviewOption;
    }

    public void setReviewOption(int reviewOption) {
        this.reviewOption = reviewOption;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAnswerType(short value) {
        this.answerType = value;
    }

    public short getAnswerType() {
        return this.answerType;
    }
}
