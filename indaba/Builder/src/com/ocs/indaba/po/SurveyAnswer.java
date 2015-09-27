/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "survey_answer")
@NamedQueries({
    @NamedQuery(name = "SurveyAnswer.findAll", query = "SELECT s FROM SurveyAnswer s"),
    @NamedQuery(name = "SurveyAnswer.findById", query = "SELECT s FROM SurveyAnswer s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyAnswer.findBySurveyContentObjectId", query = "SELECT s FROM SurveyAnswer s WHERE s.surveyContentObjectId = :surveyContentObjectId"),
    @NamedQuery(name = "SurveyAnswer.findBySurveyQuestionId", query = "SELECT s FROM SurveyAnswer s WHERE s.surveyQuestionId = :surveyQuestionId"),
    @NamedQuery(name = "SurveyAnswer.findByAnswerObjectId", query = "SELECT s FROM SurveyAnswer s WHERE s.answerObjectId = :answerObjectId"),
    @NamedQuery(name = "SurveyAnswer.findByReferenceObjectId", query = "SELECT s FROM SurveyAnswer s WHERE s.referenceObjectId = :referenceObjectId"),
    @NamedQuery(name = "SurveyAnswer.findByAnswerTime", query = "SELECT s FROM SurveyAnswer s WHERE s.answerTime = :answerTime"),
    @NamedQuery(name = "SurveyAnswer.findByAnswerUserId", query = "SELECT s FROM SurveyAnswer s WHERE s.answerUserId = :answerUserId"),
    @NamedQuery(name = "SurveyAnswer.findByInternalMsgboardId", query = "SELECT s FROM SurveyAnswer s WHERE s.internalMsgboardId = :internalMsgboardId"),
    @NamedQuery(name = "SurveyAnswer.findByStaffAuthorMsgboardId", query = "SELECT s FROM SurveyAnswer s WHERE s.staffAuthorMsgboardId = :staffAuthorMsgboardId"),
    @NamedQuery(name = "SurveyAnswer.findByReviewerHasProblem", query = "SELECT s FROM SurveyAnswer s WHERE s.reviewerHasProblem = :reviewerHasProblem"),
    @NamedQuery(name = "SurveyAnswer.findByProblemSubmitTime", query = "SELECT s FROM SurveyAnswer s WHERE s.problemSubmitTime = :problemSubmitTime"),
    @NamedQuery(name = "SurveyAnswer.findByProblemRespTime", query = "SELECT s FROM SurveyAnswer s WHERE s.problemRespTime = :problemRespTime"),
    @NamedQuery(name = "SurveyAnswer.findByStaffReviewed", query = "SELECT s FROM SurveyAnswer s WHERE s.staffReviewed = :staffReviewed"),
    @NamedQuery(name = "SurveyAnswer.findByEdited", query = "SELECT s FROM SurveyAnswer s WHERE s.edited = :edited"),
    @NamedQuery(name = "SurveyAnswer.findByPrReviewed", query = "SELECT s FROM SurveyAnswer s WHERE s.prReviewed = :prReviewed"),
    @NamedQuery(name = "SurveyAnswer.findByOverallReviewed", query = "SELECT s FROM SurveyAnswer s WHERE s.overallReviewed = :overallReviewed"),
    @NamedQuery(name = "SurveyAnswer.findByAuthorResponded", query = "SELECT s FROM SurveyAnswer s WHERE s.authorResponded = :authorResponded"),
    @NamedQuery(name = "SurveyAnswer.findByCompleted", query = "SELECT s FROM SurveyAnswer s WHERE s.completed = :completed")})
public class SurveyAnswer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_content_object_id")
    private int surveyContentObjectId;
    @Basic(optional = false)
    @Column(name = "survey_question_id")
    private int surveyQuestionId;
    @Column(name = "answer_object_id")
    private Integer answerObjectId;
    @Column(name = "reference_object_id")
    private Integer referenceObjectId;
    @Lob
    @Column(name = "comments")
    private String comments;
    @Column(name = "answer_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date answerTime;
    @Column(name = "answer_user_id")
    private Integer answerUserId;
    @Column(name = "internal_msgboard_id")
    private Integer internalMsgboardId;
    @Column(name = "staff_author_msgboard_id")
    private Integer staffAuthorMsgboardId;
    @Basic(optional = false)
    @Column(name = "reviewer_has_problem")
    private boolean reviewerHasProblem;
    @Column(name = "problem_submit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date problemSubmitTime;
    @Column(name = "problem_resp_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date problemRespTime;
    @Basic(optional = false)
    @Column(name = "staff_reviewed")
    private boolean staffReviewed;
    @Basic(optional = false)
    @Column(name = "edited")
    private boolean edited;
    @Basic(optional = false)
    @Column(name = "pr_reviewed")
    private boolean prReviewed;
    @Basic(optional = false)
    @Column(name = "overall_reviewed")
    private boolean overallReviewed;
    @Basic(optional = false)
    @Column(name = "author_responded")
    private boolean authorResponded;
    @Basic(optional = false)
    @Column(name = "completed")
    private boolean completed;

    public SurveyAnswer() {
    }

    public SurveyAnswer(Integer id) {
        this.id = id;
    }

    public SurveyAnswer(Integer id, int surveyContentObjectId, int surveyQuestionId, boolean reviewerHasProblem, boolean staffReviewed, boolean edited, boolean prReviewed, boolean overallReviewed, boolean authorResponded, boolean completed) {
        this.id = id;
        this.surveyContentObjectId = surveyContentObjectId;
        this.surveyQuestionId = surveyQuestionId;
        this.reviewerHasProblem = reviewerHasProblem;
        this.staffReviewed = staffReviewed;
        this.edited = edited;
        this.prReviewed = prReviewed;
        this.overallReviewed = overallReviewed;
        this.authorResponded = authorResponded;
        this.completed = completed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyContentObjectId() {
        return surveyContentObjectId;
    }

    public void setSurveyContentObjectId(int surveyContentObjectId) {
        this.surveyContentObjectId = surveyContentObjectId;
    }

    public int getSurveyQuestionId() {
        return surveyQuestionId;
    }

    public void setSurveyQuestionId(int surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
    }

    public Integer getAnswerObjectId() {
        return answerObjectId;
    }

    public void setAnswerObjectId(Integer answerObjectId) {
        this.answerObjectId = answerObjectId;
    }

    public Integer getReferenceObjectId() {
        return referenceObjectId;
    }

    public void setReferenceObjectId(Integer referenceObjectId) {
        this.referenceObjectId = referenceObjectId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public Integer getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(Integer answerUserId) {
        this.answerUserId = answerUserId;
    }

    public Integer getInternalMsgboardId() {
        return internalMsgboardId;
    }

    public void setInternalMsgboardId(Integer internalMsgboardId) {
        this.internalMsgboardId = internalMsgboardId;
    }

    public Integer getStaffAuthorMsgboardId() {
        return staffAuthorMsgboardId;
    }

    public void setStaffAuthorMsgboardId(Integer staffAuthorMsgboardId) {
        this.staffAuthorMsgboardId = staffAuthorMsgboardId;
    }

    public boolean getReviewerHasProblem() {
        return reviewerHasProblem;
    }

    public void setReviewerHasProblem(boolean reviewerHasProblem) {
        this.reviewerHasProblem = reviewerHasProblem;
    }

    public Date getProblemSubmitTime() {
        return problemSubmitTime;
    }

    public void setProblemSubmitTime(Date problemSubmitTime) {
        this.problemSubmitTime = problemSubmitTime;
    }

    public Date getProblemRespTime() {
        return problemRespTime;
    }

    public void setProblemRespTime(Date problemRespTime) {
        this.problemRespTime = problemRespTime;
    }

    public boolean getStaffReviewed() {
        return staffReviewed;
    }

    public void setStaffReviewed(boolean staffReviewed) {
        this.staffReviewed = staffReviewed;
    }

    public boolean getEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public boolean getPrReviewed() {
        return prReviewed;
    }

    public void setPrReviewed(boolean prReviewed) {
        this.prReviewed = prReviewed;
    }

    public boolean getOverallReviewed() {
        return overallReviewed;
    }

    public void setOverallReviewed(boolean overallReviewed) {
        this.overallReviewed = overallReviewed;
    }

    public boolean getAuthorResponded() {
        return authorResponded;
    }

    public void setAuthorResponded(boolean authorResponded) {
        this.authorResponded = authorResponded;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyAnswer)) {
            return false;
        }
        SurveyAnswer other = (SurveyAnswer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyAnswer[id=" + id + "]";
    }

}
