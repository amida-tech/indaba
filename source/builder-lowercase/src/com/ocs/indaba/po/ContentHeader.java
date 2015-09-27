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
@Table(name = "content_header")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContentHeader.findAll", query = "SELECT c FROM ContentHeader c"),
    @NamedQuery(name = "ContentHeader.findById", query = "SELECT c FROM ContentHeader c WHERE c.id = :id"),
    @NamedQuery(name = "ContentHeader.findByProjectId", query = "SELECT c FROM ContentHeader c WHERE c.projectId = :projectId"),
    @NamedQuery(name = "ContentHeader.findByContentType", query = "SELECT c FROM ContentHeader c WHERE c.contentType = :contentType"),
    @NamedQuery(name = "ContentHeader.findByContentObjectId", query = "SELECT c FROM ContentHeader c WHERE c.contentObjectId = :contentObjectId"),
    @NamedQuery(name = "ContentHeader.findByHorseId", query = "SELECT c FROM ContentHeader c WHERE c.horseId = :horseId"),
    @NamedQuery(name = "ContentHeader.findByTitle", query = "SELECT c FROM ContentHeader c WHERE c.title = :title"),
    @NamedQuery(name = "ContentHeader.findByAuthorUserId", query = "SELECT c FROM ContentHeader c WHERE c.authorUserId = :authorUserId"),
    @NamedQuery(name = "ContentHeader.findByCreateTime", query = "SELECT c FROM ContentHeader c WHERE c.createTime = :createTime"),
    @NamedQuery(name = "ContentHeader.findByLastUpdateTime", query = "SELECT c FROM ContentHeader c WHERE c.lastUpdateTime = :lastUpdateTime"),
    @NamedQuery(name = "ContentHeader.findByLastUpdateUserId", query = "SELECT c FROM ContentHeader c WHERE c.lastUpdateUserId = :lastUpdateUserId"),
    @NamedQuery(name = "ContentHeader.findByDeleteTime", query = "SELECT c FROM ContentHeader c WHERE c.deleteTime = :deleteTime"),
    @NamedQuery(name = "ContentHeader.findByDeletedByUserId", query = "SELECT c FROM ContentHeader c WHERE c.deletedByUserId = :deletedByUserId"),
    @NamedQuery(name = "ContentHeader.findByStatus", query = "SELECT c FROM ContentHeader c WHERE c.status = :status"),
    @NamedQuery(name = "ContentHeader.findByInternalMsgboardId", query = "SELECT c FROM ContentHeader c WHERE c.internalMsgboardId = :internalMsgboardId"),
    @NamedQuery(name = "ContentHeader.findByStaffAuthorMsgboardId", query = "SELECT c FROM ContentHeader c WHERE c.staffAuthorMsgboardId = :staffAuthorMsgboardId"),
    @NamedQuery(name = "ContentHeader.findByEditable", query = "SELECT c FROM ContentHeader c WHERE c.editable = :editable"),
    @NamedQuery(name = "ContentHeader.findByReviewable", query = "SELECT c FROM ContentHeader c WHERE c.reviewable = :reviewable"),
    @NamedQuery(name = "ContentHeader.findByPeerReviewable", query = "SELECT c FROM ContentHeader c WHERE c.peerReviewable = :peerReviewable"),
    @NamedQuery(name = "ContentHeader.findByApprovable", query = "SELECT c FROM ContentHeader c WHERE c.approvable = :approvable"),
    @NamedQuery(name = "ContentHeader.findBySubmitTime", query = "SELECT c FROM ContentHeader c WHERE c.submitTime = :submitTime")})
public class ContentHeader implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "project_id")
    private int projectId;
    @Basic(optional = false)
    @Column(name = "content_type")
    private int contentType;
    @Basic(optional = false)
    @Column(name = "content_object_id")
    private int contentObjectId;
    @Basic(optional = false)
    @Column(name = "horse_id")
    private int horseId;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "author_user_id")
    private Integer authorUserId;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "last_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Column(name = "last_update_user_id")
    private Integer lastUpdateUserId;
    @Column(name = "delete_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;
    @Column(name = "deleted_by_user_id")
    private Integer deletedByUserId;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "internal_msgboard_id")
    private Integer internalMsgboardId;
    @Column(name = "staff_author_msgboard_id")
    private Integer staffAuthorMsgboardId;
    @Column(name = "editable")
    private Boolean editable;
    @Column(name = "reviewable")
    private Boolean reviewable;
    @Column(name = "peer_reviewable")
    private Boolean peerReviewable;
    @Column(name = "approvable")
    private Boolean approvable;
    @Column(name = "submit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitTime;

    public ContentHeader() {
    }

    public ContentHeader(Integer id) {
        this.id = id;
    }

    public ContentHeader(Integer id, int projectId, int contentType, int contentObjectId, int horseId, String title, Date createTime, short status) {
        this.id = id;
        this.projectId = projectId;
        this.contentType = contentType;
        this.contentObjectId = contentObjectId;
        this.horseId = horseId;
        this.title = title;
        this.createTime = createTime;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getContentObjectId() {
        return contentObjectId;
    }

    public void setContentObjectId(int contentObjectId) {
        this.contentObjectId = contentObjectId;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(Integer authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(Integer lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getDeletedByUserId() {
        return deletedByUserId;
    }

    public void setDeletedByUserId(Integer deletedByUserId) {
        this.deletedByUserId = deletedByUserId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
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

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getReviewable() {
        return reviewable;
    }

    public void setReviewable(Boolean reviewable) {
        this.reviewable = reviewable;
    }

    public Boolean getPeerReviewable() {
        return peerReviewable;
    }

    public void setPeerReviewable(Boolean peerReviewable) {
        this.peerReviewable = peerReviewable;
    }

    public Boolean getApprovable() {
        return approvable;
    }

    public void setApprovable(Boolean approvable) {
        this.approvable = approvable;
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
        if (!(object instanceof ContentHeader)) {
            return false;
        }
        ContentHeader other = (ContentHeader) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ContentHeader[ id=" + id + " ]";
    }
    
}
