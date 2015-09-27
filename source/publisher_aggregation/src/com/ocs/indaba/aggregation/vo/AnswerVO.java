/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author Jeff Jiang
 */
public class AnswerVO {

    private int id;
    private int indicatorId;
    private int choiceId;
    private double score;
    private boolean useScore;
    private int choices;
    private String inputValue;
    private String criteria;
    private String tip;
    private String label;
    private int referenceObjectId;
    private int referenceId;
    private int answerUserId;
    private String referenceName = null;
    private String comments = null;
    private String references = null;
    private String reviews = null;
    private int internalMsgboardId;
    private int staffAuthorMsgboardId;
    private boolean completed = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public int getReferenceObjectId() {
        return referenceObjectId;
    }

    public void setReferenceObjectId(int referenceObjectId) {
        this.referenceObjectId = referenceObjectId;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean hasUseScore() {
        return useScore;
    }

    public void setUseScore(boolean useScore) {
        this.useScore = useScore;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(int answerUserId) {
        this.answerUserId = answerUserId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getInternalMsgboardId() {
        return internalMsgboardId;
    }

    public void setInternalMsgboardId(int internalMsgboardId) {
        this.internalMsgboardId = internalMsgboardId;
    }

    public int getStaffAuthorMsgboardId() {
        return staffAuthorMsgboardId;
    }

    public void setStaffAuthorMsgboardId(int staffAuthorMsgboardId) {
        this.staffAuthorMsgboardId = staffAuthorMsgboardId;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getChoices() {
        return choices;
    }

    public void setChoices(int choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "AnswerVO{" + "id=" + id + ", indicatorId=" + indicatorId + ", choiceId=" + choiceId + ", score=" + score + ", useScore=" + useScore + ", choices=" + choices + ", inputValue=" + inputValue + ", criteria=" + criteria + ", tip=" + tip + ", label=" + label + ", referenceObjectId=" + referenceObjectId + ", referenceId=" + referenceId + ", answerUserId=" + answerUserId + ", referenceName=" + referenceName + ", comments=" + comments + ", references=" + references + ", reviews=" + reviews + ", internalMsgboardId=" + internalMsgboardId + ", staffAuthorMsgboardId=" + staffAuthorMsgboardId + ", completed=" + completed + '}';
    }

}
