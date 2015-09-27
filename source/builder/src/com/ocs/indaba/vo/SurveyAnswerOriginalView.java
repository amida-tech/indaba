/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.User;
import java.util.List;

/**
 *
 * @author luwb
 */
public class SurveyAnswerOriginalView {
    private int contentVersionId;
    private int surveyAnswerId;
    private int answerType;
    private int answerTypeId;
    private String name;
    private String publicName;
    private String question;
    private int referType;
    private int referId;
    private String referName;
    private String refdescrition;
    private String taskName;
    private String createTime;
    private User user;
    private String userName;

    /*answers*/
    private int answerObjectId;
    private int referenceObjectId;
    private String comments;

    private List<SurveyCategoryView> categoryViewList;

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

    public String getUserName() {
        return userName;
    }

    /**
     * @param name the name to set
     */
    public void setUserName(String name) {
        this.userName = name;
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

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * @return the createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public boolean isPreVersionMode() {
        return true;
    }


    public void setContentVersionId(int id) {
        this.contentVersionId = id;
    }

    public int getContentVersionId() {
        return contentVersionId;
    }
}
