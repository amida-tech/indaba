/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "survey_peer_review")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyPeerReview.findAll", query = "SELECT s FROM SurveyPeerReview s"),
    @NamedQuery(name = "SurveyPeerReview.findById", query = "SELECT s FROM SurveyPeerReview s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyPeerReview.findBySurveyAnswerId", query = "SELECT s FROM SurveyPeerReview s WHERE s.surveyAnswerId = :surveyAnswerId"),
    @NamedQuery(name = "SurveyPeerReview.findByOpinion", query = "SELECT s FROM SurveyPeerReview s WHERE s.opinion = :opinion"),
    @NamedQuery(name = "SurveyPeerReview.findBySuggestedAnswerObjectId", query = "SELECT s FROM SurveyPeerReview s WHERE s.suggestedAnswerObjectId = :suggestedAnswerObjectId"),
    @NamedQuery(name = "SurveyPeerReview.findByLastChangeTime", query = "SELECT s FROM SurveyPeerReview s WHERE s.lastChangeTime = :lastChangeTime"),
    @NamedQuery(name = "SurveyPeerReview.findByReviewerUserId", query = "SELECT s FROM SurveyPeerReview s WHERE s.reviewerUserId = :reviewerUserId"),
    @NamedQuery(name = "SurveyPeerReview.findByMsgboardId", query = "SELECT s FROM SurveyPeerReview s WHERE s.msgboardId = :msgboardId"),
    @NamedQuery(name = "SurveyPeerReview.findBySubmitTime", query = "SELECT s FROM SurveyPeerReview s WHERE s.submitTime = :submitTime")})
public class SurveyPeerReview implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_answer_id")
    private int surveyAnswerId;
    @Basic(optional = false)
    @Column(name = "opinion")
    private short opinion;
    @Column(name = "suggested_answer_object_id")
    private Integer suggestedAnswerObjectId;
    @Lob
    @Column(name = "comments")
    private String comments;
    @Basic(optional = false)
    @Column(name = "last_change_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastChangeTime;
    @Basic(optional = false)
    @Column(name = "reviewer_user_id")
    private int reviewerUserId;
    @Basic(optional = false)
    @Column(name = "msgboard_id")
    private int msgboardId;
    @Column(name = "submit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitTime;

    public SurveyPeerReview() {
    }

    public SurveyPeerReview(Integer id) {
        this.id = id;
    }

    public SurveyPeerReview(Integer id, int surveyAnswerId, short opinion, Date lastChangeTime, int reviewerUserId, int msgboardId) {
        this.id = id;
        this.surveyAnswerId = surveyAnswerId;
        this.opinion = opinion;
        this.lastChangeTime = lastChangeTime;
        this.reviewerUserId = reviewerUserId;
        this.msgboardId = msgboardId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyAnswerId() {
        return surveyAnswerId;
    }

    public void setSurveyAnswerId(int surveyAnswerId) {
        this.surveyAnswerId = surveyAnswerId;
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

    public int getMsgboardId() {
        return msgboardId;
    }

    public void setMsgboardId(int msgboardId) {
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
        if (!(object instanceof SurveyPeerReview)) {
            return false;
        }
        SurveyPeerReview other = (SurveyPeerReview) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyPeerReview[ id=" + id + " ]";
    }
    
}
