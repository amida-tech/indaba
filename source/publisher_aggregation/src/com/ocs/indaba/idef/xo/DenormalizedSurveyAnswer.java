/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

import java.sql.Date;

/**
 *
 * @author yc06x
 */
public class DenormalizedSurveyAnswer {

    private int projectId;
    private String projectName;
    private int productId;
    private String productName;
    private short isTsc;
    private Integer categoryId;
    private String categoryTitle;
    private Integer categoryWeight;
    private int questionId;
    private String questionLabel;
    private int questionWeight;
    private int indicatorId;
    private String indicatorQuestion;
    private int indicatorDataType;
    private int targetId;
    private String targetName;
    private String targetDescription;
    private int answerId;
    private Integer answerUserId;
    private String answerUserFirstName;
    private String answerUserLastName;
    private Date answerTime;
    private Integer answerValueChoices;
    private Integer answerValueInt;
    private Float answerValueFloat;
    private String answerValueText;
    private String answerComments;
    private Integer referenceId;
    private Integer refChoices;
    private String refSourceDescription;
    private Integer reviewUserId;
    private String reviewUserRole;
    private String reviewUserFirstName;
    private String reviewUserLastName;
    private Integer reviewOpinion;
    private String reviewComments;
    private Integer reviewAnswerValueChoices;
    private Integer reviewAnswerValueInt;
    private Float reviewAnswerValueFloat;
    private String reviewAnswerValueText;

    public void setProjectId(int v) {
        this.projectId = v;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public void setProjectName(String v) {
        this.projectName = v;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProductId(int v) {
        this.productId = v;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductName(String v) {
        this.productName = v;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setCategoryId(Integer v) {
        this.categoryId = v;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryWeight(Integer v) {
        this.categoryWeight = v;
    }

    public Integer getCategoryWeight() {
        return this.categoryWeight;
    }

    public void setCategoryTitle(String v) {
        this.categoryTitle = v;
    }

    public String getCategoryTitle() {
        return this.categoryTitle;
    }

    public void setQuestionId(int v) {
        this.questionId = v;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    public void setQuestionWeight(int v) {
        this.questionWeight = v;
    }

    public int getQuestionWeight() {
        return this.questionWeight;
    }

    public void setQuestionLabel(String v) {
        this.questionLabel = v;
    }

    public String getQuestionLabel() {
        return this.questionLabel;
    }

    public void setIndicatorId(int v) {
        this.indicatorId = v;
    }

    public int getIndicatorId() {
        return this.indicatorId;
    }

    public void setIndicatorQuestion(String v) {
        this.indicatorQuestion = v;
    }

    public String getIndicatorQuestion() {
        return this.indicatorQuestion;
    }

    public void setIndicatorDataType(int v) {
        this.indicatorDataType = v;
    }

    public int getIndicatorDataType() {
        return this.indicatorDataType;
    }

    public void setTargetId(int v) {
        this.targetId = v;
    }

    public int getTargetId() {
        return this.targetId;
    }

    public void setTargetName(String v) {
        this.targetName = v;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetDescription(String v) {
        this.targetDescription = v;
    }

    public String getTargetDescription() {
        return this.targetDescription;
    }


    public void setAnswerId(int v) {
        this.answerId = v;
    }

    public int getAnswerId() {
        return this.answerId;
    }

    public void setAnswerUserId(Integer v) {
        this.answerUserId = v;
    }

    public Integer getAnswerUserId() {
        return this.answerUserId;
    }

    public void setAnswerUserFirstName(String v) {
        this.answerUserFirstName = v;
    }

    public String getAnswerUserFirstName() {
        return this.answerUserFirstName;
    }

    public void setAnswerUserLastName(String v) {
        this.answerUserLastName = v;
    }

    public String getAnswerUserLastName() {
        return this.answerUserLastName;
    }


    public void setAnswerTime(Date v) {
        this.answerTime = v;
    }

    public Date getAnswerTime() {
        return this.answerTime;
    }

    public void setAnswerValueChoices(Integer v) {
        this.answerValueChoices = v;
    }

    public Integer getAnswerValueChoices() {
        return this.answerValueChoices;
    }

    public void setAnswerValueInt(Integer v) {
        this.answerValueInt = v;
    }

    public Integer getAnswerValueInt() {
        return this.answerValueInt;
    }

    public void setAnswerValueFloat(Float v) {
        this.answerValueFloat = v;
    }

    public Float getAnswerValueFloat() {
        return this.answerValueFloat;
    }

    public void setAnswerValueText(String v) {
        this.answerValueText = v;
    }

    public String getAnswerValueText() {
        return this.answerValueText;
    }

    public void setAnswerComments(String v) {
        this.answerComments = v;
    }

    public String getAnswerComments() {
        return this.answerComments;
    }

    public void setReferenceId(Integer v) {
        this.referenceId = v;
    }

    public Integer getReferenceId() {
        return this.referenceId;
    }

    public void setRefChoices(Integer v) {
        this.refChoices = v;
    }

    public Integer getRefChoices() {
        return this.refChoices;
    }

    public void setRefSourceDescription(String v) {
        this.refSourceDescription = v;
    }

    public String getRefSourceDescription() {
        return this.refSourceDescription;
    }

    public void setReviewUserId(Integer v) {
        this.reviewUserId = v;
    }

    public Integer getReviewUserId() {
        return this.reviewUserId;
    }

    public void setReviewUserFirstName(String v) {
        this.reviewUserFirstName = v;
    }

    public String getReviewUserFirstName() {
        return this.reviewUserFirstName;
    }

    public void setReviewUserLastName(String v) {
        this.reviewUserLastName = v;
    }

    public String getReviewUserLastName() {
        return this.reviewUserLastName;
    }

    public void setReviewUserRole(String v) {
        this.reviewUserRole = v;
    }

    public String getReviewUserRole() {
        return this.reviewUserRole;
    }

    public void setReviewOpinion(Integer v) {
        this.reviewOpinion = v;
    }

    public Integer getReviewOpinion() {
        return this.reviewOpinion;
    }

    public void setReviewComments(String v) {
        this.reviewComments = v;
    }

    public String getReviewComments() {
        return this.reviewComments;
    }


    public void setReviewAnswerValueChoices(Integer v) {
        this.reviewAnswerValueChoices = v;
    }

    public Integer getReviewAnswerValueChoices() {
        return this.reviewAnswerValueChoices;
    }

    public void setReviewAnswerValueInt(Integer v) {
        this.reviewAnswerValueInt = v;
    }

    public Integer getReviewAnswerValueInt() {
        return this.reviewAnswerValueInt;
    }

    public void setReviewAnswerValueFloat(Float v) {
        this.reviewAnswerValueFloat = v;
    }

    public Float getReviewAnswerValueFloat() {
        return this.reviewAnswerValueFloat;
    }

    public void setReviewAnswerValueText(String v) {
        this.reviewAnswerValueText = v;
    }

    public String getReviewAnswerValueText() {
        return this.reviewAnswerValueText;
    }

    public void setIsTsc(short v) {
        this.isTsc = v;
    }

    public short getIsTsc() {
        return this.isTsc;
    }

}
