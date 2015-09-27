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
@Table(name = "journal_peer_review")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JournalPeerReview.findAll", query = "SELECT j FROM JournalPeerReview j"),
    @NamedQuery(name = "JournalPeerReview.findById", query = "SELECT j FROM JournalPeerReview j WHERE j.id = :id"),
    @NamedQuery(name = "JournalPeerReview.findByJournalContentObjectId", query = "SELECT j FROM JournalPeerReview j WHERE j.journalContentObjectId = :journalContentObjectId"),
    @NamedQuery(name = "JournalPeerReview.findByReviewerUserId", query = "SELECT j FROM JournalPeerReview j WHERE j.reviewerUserId = :reviewerUserId"),
    @NamedQuery(name = "JournalPeerReview.findByLastChangeTime", query = "SELECT j FROM JournalPeerReview j WHERE j.lastChangeTime = :lastChangeTime"),
    @NamedQuery(name = "JournalPeerReview.findByMsgboardId", query = "SELECT j FROM JournalPeerReview j WHERE j.msgboardId = :msgboardId"),
    @NamedQuery(name = "JournalPeerReview.findBySubmitTime", query = "SELECT j FROM JournalPeerReview j WHERE j.submitTime = :submitTime")})
public class JournalPeerReview implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "journal_content_object_id")
    private int journalContentObjectId;
    @Basic(optional = false)
    @Column(name = "reviewer_user_id")
    private int reviewerUserId;
    @Lob
    @Column(name = "opinions")
    private String opinions;
    @Column(name = "last_change_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastChangeTime;
    @Column(name = "msgboard_id")
    private Integer msgboardId;
    @Column(name = "submit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitTime;

    public JournalPeerReview() {
    }

    public JournalPeerReview(Integer id) {
        this.id = id;
    }

    public JournalPeerReview(Integer id, int journalContentObjectId, int reviewerUserId) {
        this.id = id;
        this.journalContentObjectId = journalContentObjectId;
        this.reviewerUserId = reviewerUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getJournalContentObjectId() {
        return journalContentObjectId;
    }

    public void setJournalContentObjectId(int journalContentObjectId) {
        this.journalContentObjectId = journalContentObjectId;
    }

    public int getReviewerUserId() {
        return reviewerUserId;
    }

    public void setReviewerUserId(int reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
    }

    public String getOpinions() {
        return opinions;
    }

    public void setOpinions(String opinions) {
        this.opinions = opinions;
    }

    public Date getLastChangeTime() {
        return lastChangeTime;
    }

    public void setLastChangeTime(Date lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
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
        if (!(object instanceof JournalPeerReview)) {
            return false;
        }
        JournalPeerReview other = (JournalPeerReview) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.JournalPeerReview[ id=" + id + " ]";
    }
    
}
