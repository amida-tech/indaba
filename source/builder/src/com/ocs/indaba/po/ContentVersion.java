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
@Table(name = "content_version")
@NamedQueries({
    @NamedQuery(name = "ContentVersion.findAll", query = "SELECT c FROM ContentVersion c"),
    @NamedQuery(name = "ContentVersion.findById", query = "SELECT c FROM ContentVersion c WHERE c.id = :id"),
    @NamedQuery(name = "ContentVersion.findByContentHeaderId", query = "SELECT c FROM ContentVersion c WHERE c.contentHeaderId = :contentHeaderId"),
    @NamedQuery(name = "ContentVersion.findByCreateTime", query = "SELECT c FROM ContentVersion c WHERE c.createTime = :createTime"),
    @NamedQuery(name = "ContentVersion.findByUserId", query = "SELECT c FROM ContentVersion c WHERE c.userId = :userId"),
    @NamedQuery(name = "ContentVersion.findByDescription", query = "SELECT c FROM ContentVersion c WHERE c.description = :description"),
    @NamedQuery(name = "ContentVersion.findByInternalMsgboardId", query = "SELECT c FROM ContentVersion c WHERE c.internalMsgboardId = :internalMsgboardId"),
    @NamedQuery(name = "ContentVersion.findByStaffAuthorMsgboardId", query = "SELECT c FROM ContentVersion c WHERE c.staffAuthorMsgboardId = :staffAuthorMsgboardId")})
public class ContentVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "content_header_id")
    private int contentHeaderId;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "description")
    private String description;
    @Column(name = "internal_msgboard_id")
    private Integer internalMsgboardId;
    @Column(name = "staff_author_msgboard_id")
    private Integer staffAuthorMsgboardId;

    public ContentVersion() {
    }

    public ContentVersion(Integer id) {
        this.id = id;
    }

    public ContentVersion(Integer id, int contentHeaderId, Date createTime, int userId) {
        this.id = id;
        this.contentHeaderId = contentHeaderId;
        this.createTime = createTime;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getContentHeaderId() {
        return contentHeaderId;
    }

    public void setContentHeaderId(int contentHeaderId) {
        this.contentHeaderId = contentHeaderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof ContentVersion)) {
            return false;
        }
        ContentVersion other = (ContentVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ContentVersion[id=" + id + "]";
    }

}
