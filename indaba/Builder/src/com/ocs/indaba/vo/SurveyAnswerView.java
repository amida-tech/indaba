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
public class SurveyAnswerView {

    private int surveyAnswerId;
    private int answerType;
    private int answerTypeId;
    private String name;
    private String publicName;
    private String question;
    private String tip;
    private int referType;
    private int referId;
    private String referName;
    private String refdescrition;
    private List<SurveyCategoryView> categoryViewList;
    private short tipDisplayMethod;

    /*answers*/
    private int answerObjectId;
    private int referenceObjectId;
    private String comments;
    /*previous version*/
    private boolean preVersionMode;
    private int initialFlagGroupId = 0;

    /**
     * @return the surveyAnswerId
     */
    public int getSurveyAnswerId() {
        return surveyAnswerId;
    }

    /**
     * @param surveyAnswerId the surveyAnswerId to set
     */
    public void setSurveyAnswerId(int surveyAnswerId) {
        this.surveyAnswerId = surveyAnswerId;
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
     * @return the referType
     */
    public int getReferType() {
        return referType;
    }

    /**
     * @param referType the referType to set
     */
    public void setReferType(int referType) {
        this.referType = referType;
    }

    /**
     * @return the referId
     */
    public int getReferId() {
        return referId;
    }

    /**
     * @param referId the referId to set
     */
    public void setReferId(int referId) {
        this.referId = referId;
    }

    /**
     * @return the referName
     */
    public String getReferName() {
        return referName;
    }

    /**
     * @param referName the referName to set
     */
    public void setReferName(String referName) {
        this.referName = referName;
    }

    /**
     * @return the refdescrition
     */
    public String getRefdescrition() {
        return refdescrition;
    }

    /**
     * @param refdescrition the refdescrition to set
     */
    public void setRefdescrition(String refdescrition) {
        this.refdescrition = refdescrition;
    }

    /**
     * @return the categoryViewList
     */
    public List<SurveyCategoryView> getCategoryViewList() {
        return categoryViewList;
    }

    /**
     * @param categoryViewList the categoryViewList to set
     */
    public void setCategoryViewList(List<SurveyCategoryView> categoryViewList) {
        this.categoryViewList = categoryViewList;
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

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public boolean isPreVersionMode() {
        return preVersionMode;
    }

    public void setPreVersionMode(boolean preVersionMode) {
        this.preVersionMode = preVersionMode;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }

    public void setTipDisplayMethod(short value) {
        this.tipDisplayMethod = value;
    }

    public short getTipDisplayMethod() {
        return this.tipDisplayMethod;
    }

    public void setInitialFlagGroupId(int id) {
        this.initialFlagGroupId = id;
    }

    public int getInitialFlagGroupId() {
        return this.initialFlagGroupId;
    }
}
