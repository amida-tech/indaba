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
@Table(name = "survey_answer_version")
@NamedQueries({
    @NamedQuery(name = "SurveyAnswerVersion.findAll", query = "SELECT s FROM SurveyAnswerVersion s"),
    @NamedQuery(name = "SurveyAnswerVersion.findById", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyAnswerVersion.findByContentVersionId", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.contentVersionId = :contentVersionId"),
    @NamedQuery(name = "SurveyAnswerVersion.findBySurveyQuestionId", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.surveyQuestionId = :surveyQuestionId"),
    @NamedQuery(name = "SurveyAnswerVersion.findByAnswerObjectId", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.answerObjectId = :answerObjectId"),
    @NamedQuery(name = "SurveyAnswerVersion.findByReferenceObjectId", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.referenceObjectId = :referenceObjectId"),
    @NamedQuery(name = "SurveyAnswerVersion.findByAnswerTime", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.answerTime = :answerTime"),
    @NamedQuery(name = "SurveyAnswerVersion.findByAnswerUserId", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.answerUserId = :answerUserId"),
    @NamedQuery(name = "SurveyAnswerVersion.findByInternalMsgboardId", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.internalMsgboardId = :internalMsgboardId"),
    @NamedQuery(name = "SurveyAnswerVersion.findByStaffAuthorMsgboardId", query = "SELECT s FROM SurveyAnswerVersion s WHERE s.staffAuthorMsgboardId = :staffAuthorMsgboardId")})
public class SurveyAnswerVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "content_version_id")
    private int contentVersionId;
    @Basic(optional = false)
    @Column(name = "survey_question_id")
    private int surveyQuestionId;
    @Basic(optional = false)
    @Column(name = "answer_object_id")
    private int answerObjectId;
    @Basic(optional = false)
    @Column(name = "reference_object_id")
    private int referenceObjectId;
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

    public SurveyAnswerVersion() {
    }

    public SurveyAnswerVersion(Integer id) {
        this.id = id;
    }

    public SurveyAnswerVersion(Integer id, int contentVersionId, int surveyQuestionId, int answerObjectId, int referenceObjectId) {
        this.id = id;
        this.contentVersionId = contentVersionId;
        this.surveyQuestionId = surveyQuestionId;
        this.answerObjectId = answerObjectId;
        this.referenceObjectId = referenceObjectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getContentVersionId() {
        return contentVersionId;
    }

    public void setContentVersionId(int contentVersionId) {
        this.contentVersionId = contentVersionId;
    }

    public int getSurveyQuestionId() {
        return surveyQuestionId;
    }

    public void setSurveyQuestionId(int surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
    }

    public int getAnswerObjectId() {
        return answerObjectId;
    }

    public void setAnswerObjectId(int answerObjectId) {
        this.answerObjectId = answerObjectId;
    }

    public int getReferenceObjectId() {
        return referenceObjectId;
    }

    public void setReferenceObjectId(int referenceObjectId) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyAnswerVersion)) {
            return false;
        }
        SurveyAnswerVersion other = (SurveyAnswerVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyAnswerVersion[id=" + id + "]";
    }

}
