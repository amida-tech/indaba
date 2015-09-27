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
@Table(name = "groupobj_flag")
@NamedQueries({
    @NamedQuery(name = "GroupobjFlag.findAll", query = "SELECT g FROM GroupobjFlag g"),
    @NamedQuery(name = "GroupobjFlag.findById", query = "SELECT g FROM GroupobjFlag g WHERE g.id = :id"),
    @NamedQuery(name = "GroupobjFlag.findByGroupobjId", query = "SELECT g FROM GroupobjFlag g WHERE g.groupobjId = :groupobjId"),
    @NamedQuery(name = "GroupobjFlag.findByRaiseUserId", query = "SELECT g FROM GroupobjFlag g WHERE g.raiseUserId = :raiseUserId"),
    @NamedQuery(name = "GroupobjFlag.findByRaiseTime", query = "SELECT g FROM GroupobjFlag g WHERE g.raiseTime = :raiseTime"),
    @NamedQuery(name = "GroupobjFlag.findByAssignedUserId", query = "SELECT g FROM GroupobjFlag g WHERE g.assignedUserId = :assignedUserId"),
    @NamedQuery(name = "GroupobjFlag.findByDueTime", query = "SELECT g FROM GroupobjFlag g WHERE g.dueTime = :dueTime"),
    @NamedQuery(name = "GroupobjFlag.findByPermissions", query = "SELECT g FROM GroupobjFlag g WHERE g.permissions = :permissions"),
    @NamedQuery(name = "GroupobjFlag.findByRespondTime", query = "SELECT g FROM GroupobjFlag g WHERE g.respondTime = :respondTime"),
    @NamedQuery(name = "GroupobjFlag.findByUnsetTime", query = "SELECT g FROM GroupobjFlag g WHERE g.unsetTime = :unsetTime"),
    @NamedQuery(name = "GroupobjFlag.findByUnsetUserId", query = "SELECT g FROM GroupobjFlag g WHERE g.unsetUserId = :unsetUserId")})
public class GroupobjFlag implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "groupobj_id")
    private int groupobjId;
    @Basic(optional = false)
    @Column(name = "raise_user_id")
    private int raiseUserId;
    @Basic(optional = false)
    @Column(name = "raise_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date raiseTime;
    @Basic(optional = false)
    @Lob
    @Column(name = "issue_description")
    private String issueDescription;
    @Basic(optional = false)
    @Column(name = "assigned_user_id")
    private int assignedUserId;
    @Basic(optional = false)
    @Column(name = "due_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueTime;
    @Basic(optional = false)
    @Column(name = "permissions")
    private int permissions;
    @Column(name = "respond_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date respondTime;
    @Column(name = "unset_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date unsetTime;
    @Lob
    @Column(name = "unset_comment")
    private String unsetComment;
    @Column(name = "unset_user_id")
    private Integer unsetUserId;

    public GroupobjFlag() {
    }

    public GroupobjFlag(Integer id) {
        this.id = id;
    }

    public GroupobjFlag(Integer id, int groupobjId, int raiseUserId, Date raiseTime, String issueDescription, int assignedUserId, Date dueTime, int permissions) {
        this.id = id;
        this.groupobjId = groupobjId;
        this.raiseUserId = raiseUserId;
        this.raiseTime = raiseTime;
        this.issueDescription = issueDescription;
        this.assignedUserId = assignedUserId;
        this.dueTime = dueTime;
        this.permissions = permissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGroupobjId() {
        return groupobjId;
    }

    public void setGroupobjId(int groupobjId) {
        this.groupobjId = groupobjId;
    }

    public int getRaiseUserId() {
        return raiseUserId;
    }

    public void setRaiseUserId(int raiseUserId) {
        this.raiseUserId = raiseUserId;
    }

    public Date getRaiseTime() {
        return raiseTime;
    }

    public void setRaiseTime(Date raiseTime) {
        this.raiseTime = raiseTime;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public int getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public Date getRespondTime() {
        return respondTime;
    }

    public void setRespondTime(Date respondTime) {
        this.respondTime = respondTime;
    }

    public Date getUnsetTime() {
        return unsetTime;
    }

    public void setUnsetTime(Date unsetTime) {
        this.unsetTime = unsetTime;
    }

    public String getUnsetComment() {
        return unsetComment;
    }

    public void setUnsetComment(String unsetComment) {
        this.unsetComment = unsetComment;
    }

    public Integer getUnsetUserId() {
        return unsetUserId;
    }

    public void setUnsetUserId(Integer unsetUserId) {
        this.unsetUserId = unsetUserId;
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
        if (!(object instanceof GroupobjFlag)) {
            return false;
        }
        GroupobjFlag other = (GroupobjFlag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.GroupobjFlag[id=" + id + "]";
    }

}
