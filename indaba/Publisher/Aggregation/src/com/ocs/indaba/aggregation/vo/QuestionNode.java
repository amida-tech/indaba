/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.po.Attachment;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeff Jiang
 */
public class QuestionNode extends ScorecardBaseNode {
    ////////////////////////////////
    // Question

    private int questionId;
    private int questionType;
    private String questionName = null;
    private String publicName = null;
    private String questionText = null;
    private List<QuestionOption> options = null;
    private List<Attachment> attachements = null;
    ////////////////////////////////
    // Answer
    private int choiceId;
    private int choices;
    private double score;
    private String inputValue;
    private String criteria;
    private String tip;
    //private boolean useScore;
    private String label;
    private String comments = null;
    private int answerUserId;
    private int referenceObjectId;
    private int referenceId;
    private int refClassification;
    private String referenceName = null;
    private String references = null;
    private List<String> reviews = null;
    private int answerId;
    private int internalMsgboardId;
    private int staffAuthorMsgboardId;
    private boolean completed = false;

    public QuestionNode() {
        setNodeType(Constants.NODE_TYPE_QUESTION);
    }

    public QuestionNode(int id) {
        super(id);
        setNodeType(Constants.NODE_TYPE_QUESTION);
    }

    public QuestionNode(int id, String name, double score) {
        super(Constants.NODE_TYPE_QUESTION, id, name);
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    /*
    public boolean hasUseScore() {
    return useScore;
    }
    
    public void setUseScore(boolean useScore) {
    this.useScore = useScore;
    }
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    public void addOption(QuestionOption option) {
        if (options == null) {
            options = new ArrayList<QuestionOption>();
        }
        options.add(option);
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
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

    public int getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(int answerUserId) {
        this.answerUserId = answerUserId;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void addReview(String comment) {
        if (reviews == null) {
            reviews = new ArrayList<String>();
        }
        reviews.add(comment);
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
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

    public List<Attachment> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachment> attachements) {
        this.attachements = attachements;
    }

    public void addAttachement(Attachment attached) {
        if (this.attachements == null) {
            this.attachements = new ArrayList<Attachment>();
        }
        this.attachements.add(attached);
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


    public int getRefClassification() {
        return this.refClassification;
    }

    public void setRefClassification(int c) {
        this.refClassification = c;
    }

    @Override
    public String toString() {
        return "QuestionNode{" + "questionId=" + questionId + ", questionType=" + questionType + ", questionName=" + questionName + ", publicName=" + publicName + ", questionText=" + questionText + ", options=" + options + ", attachements=" + attachements + ", choiceId=" + choiceId + ", choices=" + choices + ", score=" + score + ", inputValue=" + inputValue + ", criteria=" + criteria + ", tip=" + tip + ", label=" + label + ", comments=" + comments + ", answerUserId=" + answerUserId + ", referenceObjectId=" + referenceObjectId + ", referenceId=" + referenceId + ", referenceName=" + referenceName + ", references=" + references + ", reviews=" + reviews + ", answerId=" + answerId + ", internalMsgboardId=" + internalMsgboardId + ", staffAuthorMsgboardId=" + staffAuthorMsgboardId + ", completed=" + completed + '}';
    }

}
