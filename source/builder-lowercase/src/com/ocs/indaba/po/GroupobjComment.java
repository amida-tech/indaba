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
@Table(name = "groupobj_comment")
@NamedQueries({
    @NamedQuery(name = "GroupobjComment.findAll", query = "SELECT g FROM GroupobjComment g"),
    @NamedQuery(name = "GroupobjComment.findById", query = "SELECT g FROM GroupobjComment g WHERE g.id = :id"),
    @NamedQuery(name = "GroupobjComment.findByGroupobjId", query = "SELECT g FROM GroupobjComment g WHERE g.groupobjId = :groupobjId"),
    @NamedQuery(name = "GroupobjComment.findByType", query = "SELECT g FROM GroupobjComment g WHERE g.type = :type"),
    @NamedQuery(name = "GroupobjComment.findByGroupobjFlagId", query = "SELECT g FROM GroupobjComment g WHERE g.groupobjFlagId = :groupobjFlagId"),
    @NamedQuery(name = "GroupobjComment.findByAuthorUserId", query = "SELECT g FROM GroupobjComment g WHERE g.authorUserId = :authorUserId"),
    @NamedQuery(name = "GroupobjComment.findByCreateTime", query = "SELECT g FROM GroupobjComment g WHERE g.createTime = :createTime"),
    @NamedQuery(name = "GroupobjComment.findByDeleteUserId", query = "SELECT g FROM GroupobjComment g WHERE g.deleteUserId = :deleteUserId"),
    @NamedQuery(name = "GroupobjComment.findByDeleteTime", query = "SELECT g FROM GroupobjComment g WHERE g.deleteTime = :deleteTime")})
public class GroupobjComment implements Serializable {
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
    @Column(name = "type")
    private short type;
    @Column(name = "groupobj_flag_id")
    private Integer groupobjFlagId;
    @Basic(optional = false)
    @Lob
    @Column(name = "comment")
    private String comment;
    @Basic(optional = false)
    @Column(name = "author_user_id")
    private int authorUserId;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "delete_user_id")
    private Integer deleteUserId;
    @Column(name = "delete_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;

    public GroupobjComment() {
    }

    public GroupobjComment(Integer id) {
        this.id = id;
    }

    public GroupobjComment(Integer id, int groupobjId, short type, String comment, int authorUserId, Date createTime) {
        this.id = id;
        this.groupobjId = groupobjId;
        this.type = type;
        this.comment = comment;
        this.authorUserId = authorUserId;
        this.createTime = createTime;
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

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public Integer getGroupobjFlagId() {
        return groupobjFlagId;
    }

    public void setGroupobjFlagId(Integer groupobjFlagId) {
        this.groupobjFlagId = groupobjFlagId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(int authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleteUserId() {
        return deleteUserId;
    }

    public void setDeleteUserId(Integer deleteUserId) {
        this.deleteUserId = deleteUserId;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
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
        if (!(object instanceof GroupobjComment)) {
            return false;
        }
        GroupobjComment other = (GroupobjComment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.GroupobjComment[id=" + id + "]";
    }

}
