package com.ocs.indaba.vo;

import java.util.Date;

public class SurveyPeerReviewVO {

    private UserDisplay reviewer;
    private int msgboardId;
    private int id;
    private short opinion;
    private int suggestedAnswerObjectId;
    private String comments;
    private Date lastChangeTime;
    private String suggestedScore;
    private int answerId;
    private int questionId;
    private String publicName;
    private String question;
    private boolean staffReviewed;
    private boolean prReviewed;
    private Date submitTime;
    private int surveyPeerReviewVersionId;

    public UserDisplay getReviewer() {
        return reviewer;
    }

    public void setReviewer(UserDisplay reviewer) {
        this.reviewer = reviewer;
    }

    public int getMsgboardId() {
        return msgboardId;
    }

    public void setMsgboardId(int msgboardId) {
        this.msgboardId = msgboardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getOpinion() {
        return opinion;
    }

    public void setOpinion(short opinion) {
        this.opinion = opinion;
    }

    public int getSuggestedAnswerObjectId() {
        return suggestedAnswerObjectId;
    }

    public void setSuggestedAnswerObjectId(int suggestedAnswerObjectId) {
        this.suggestedAnswerObjectId = suggestedAnswerObjectId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getLastChangeTime() {
        return lastChangeTime;
    }

    public void setLastChangeTime(Date lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

    public String getSuggestedScore() {
        return suggestedScore;
    }

    public void setSuggestedScore(String suggestedScore) {
        this.suggestedScore = suggestedScore;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isStaffReviewed() {
        return staffReviewed;
    }

    public void setStaffReviewed(boolean staffReviewed) {
        this.staffReviewed = staffReviewed;
    }

    public boolean isPrReviewed() {
        return prReviewed;
    }

    public void setPrReviewed(boolean prReviewed) {
        this.prReviewed = prReviewed;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public int getSurveyPeerReviewVersionId() {
        return surveyPeerReviewVersionId;
    }

    public void setSurveyPeerReviewVersionId(int value) {
        surveyPeerReviewVersionId = value;
    }
}
