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
 * @author Administrator
 */
@Entity
@Table(name = "survey_peer_review_version")
@NamedQueries({
    @NamedQuery(name = "SurveyPeerReviewVersion.findAll", query = "SELECT s FROM SurveyPeerReviewVersion s"),
    @NamedQuery(name = "SurveyPeerReviewVersion.findById", query = "SELECT s FROM SurveyPeerReviewVersion s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyPeerReviewVersion.findBySurveyAnswerVersionId", query = "SELECT s FROM SurveyPeerReviewVersion s WHERE s.surveyAnswerVersionId = :surveyAnswerVersionId"),
    @NamedQuery(name = "SurveyPeerReviewVersion.findByOpinion", query = "SELECT s FROM SurveyPeerReviewVersion s WHERE s.opinion = :opinion"),
    @NamedQuery(name = "SurveyPeerReviewVersion.findBySuggestedAnswerObjectId", query = "SELECT s FROM SurveyPeerReviewVersion s WHERE s.suggestedAnswerObjectId = :suggestedAnswerObjectId"),
    @NamedQuery(name = "SurveyPeerReviewVersion.findByLastChangeTime", query = "SELECT s FROM SurveyPeerReviewVersion s WHERE s.lastChangeTime = :lastChangeTime"),
    @NamedQuery(name = "SurveyPeerReviewVersion.findByReviewerUserId", query = "SELECT s FROM SurveyPeerReviewVersion s WHERE s.reviewerUserId = :reviewerUserId"),
    @NamedQuery(name = "SurveyPeerReviewVersion.findByMsgboardId", query = "SELECT s FROM SurveyPeerReviewVersion s WHERE s.msgboardId = :msgboardId"),
    @NamedQuery(name = "SurveyPeerReviewVersion.findBySubmitTime", query = "SELECT s FROM SurveyPeerReviewVersion s WHERE s.submitTime = :submitTime")})
public class SurveyPeerReviewVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_answer_version_id")
    private int surveyAnswerVersionId;
    @Basic(optional = false)
    @Column(name = "opinion")
    private short opinion;
    @Column(name = "suggested_answer_object_id")
    private Integer suggestedAnswerObjectId;
    @Lob
    @Column(name = "comments")
    private String comments;
    @Column(name = "last_change_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastChangeTime;
    @Basic(optional = false)
    @Column(name = "reviewer_user_id")
    private int reviewerUserId;
    @Column(name = "msgboard_id")
    private Integer msgboardId;
    @Column(name = "submit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitTime;

    public SurveyPeerReviewVersion() {
    }

    public SurveyPeerReviewVersion(Integer id) {
        this.id = id;
    }

    public SurveyPeerReviewVersion(Integer id, int surveyAnswerVersionId, short opinion, int reviewerUserId) {
        this.id = id;
        this.surveyAnswerVersionId = surveyAnswerVersionId;
        this.opinion = opinion;
        this.reviewerUserId = reviewerUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyAnswerVersionId() {
        return surveyAnswerVersionId;
    }

    public void setSurveyAnswerVersionId(int surveyAnswerVersionId) {
        this.surveyAnswerVersionId = surveyAnswerVersionId;
    }

    public short getOpinion() {
        return opinion;
    }

    public void setOpinion(short opinion) {
        this.opinion = opinion;
    }

    public Integer getSuggestedAnswerObjectId() {
        return suggestedAnswerObjectId;
    }

    public void setSuggestedAnswerObjectId(Integer suggestedAnswerObjectId) {
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

    public int getReviewerUserId() {
        return reviewerUserId;
    }

    public void setReviewerUserId(int reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
    }

    public Integer getMsgboardId() {
        return msgboardId;
    }

    public void setMsgboardId(Integer msgboardId) {
        this.msgboardId = msgboardId;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
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
        if (!(object instanceof SurveyPeerReviewVersion)) {
            return false;
        }
        SurveyPeerReviewVersion other = (SurveyPeerReviewVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyPeerReviewVersion[id=" + id + "]";
    }

}
