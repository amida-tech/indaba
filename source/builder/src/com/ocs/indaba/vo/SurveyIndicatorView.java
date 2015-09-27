/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author luwb
 */
public class SurveyIndicatorView {
    private int indicatorId;
    private String name;
    private String question;
    private String orgName;
    private int orgId;
    private int answerType;
    private int answerTypeId;
    private int referenceId;
    private boolean used;

    /**
     * @return the indicatorId
     */
    public int getIndicatorId() {
        return indicatorId;
    }

    /**
     * @param indicatorId the indicatorId to set
     */
    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return the answerType
     */
    public int getAnswerType() {
        return answerType;
    }

    /**
     * @param answerType the answerType to set
     */
    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    /**
     * @return the answerTypeId
     */
    public int getAnswerTypeId() {
        return answerTypeId;
    }

    /**
     * @param answerTypeId the answerTypeId to set
     */
    public void setAnswerTypeId(int answerTypeId) {
        this.answerTypeId = answerTypeId;
    }

    /**
     * @return the referenceId
     */
    public int getReferenceId() {
        return referenceId;
    }

    /**
     * @param referenceId the referenceId to set
     */
    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    
}
