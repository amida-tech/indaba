/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import java.util.List;

/**
 *
 * @author yc06x
 */
public class QuestionDef {

    private int indicatorId;
    private int questionId;
    private int questionType;
    private String questionName = null;
    private String publicName = null;
    private String questionText = null;
    private String tip = null;
    private String criteria = null;
    private List<QuestionOption> options = null;

    public QuestionDef(int indicatorId, int qstId, int type, String name, String publicName, String text, String tip, String criteria, List<QuestionOption> options) {
        this.indicatorId = indicatorId;
        this.questionId = qstId;
        this.questionType = type;
        this.questionName = name;
        this.publicName = publicName;
        this.questionText = text;
        this.tip = tip;
        this.criteria = criteria;
        this.options = options;
    }


    public int getIndicatorId() {
        return this.indicatorId;
    }


    public int getQuestionId() {
        return this.questionId;
    }


    public int getType() {
        return this.questionType;
    }

    public String getName() {
        return this.questionName;
    }

    public String getPublicName() {
        return this.publicName;
    }

    public String getText() {
        return this.questionText;
    }


    public void setText(String text) {
        this.questionText = text;
    }


    public String getTip() {
        return tip;
    }


    public void setTip(String tip) {
        this.tip = tip;
    }


    public String getCriteria() {
        return criteria;
    }



    public List<QuestionOption> getOptions() {
        return this.options;
    }

}
