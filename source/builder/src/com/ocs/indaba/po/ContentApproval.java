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
@Table(name = "content_approval")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContentApproval.findAll", query = "SELECT c FROM ContentApproval c"),
    @NamedQuery(name = "ContentApproval.findById", query = "SELECT c FROM ContentApproval c WHERE c.id = :id"),
    @NamedQuery(name = "ContentApproval.findByContentHeaderId", query = "SELECT c FROM ContentApproval c WHERE c.contentHeaderId = :contentHeaderId"),
    @NamedQuery(name = "ContentApproval.findByUserId", query = "SELECT c FROM ContentApproval c WHERE c.userId = :userId"),
    @NamedQuery(name = "ContentApproval.findByTime", query = "SELECT c FROM ContentApproval c WHERE c.time = :time"),
    @NamedQuery(name = "ContentApproval.findByTaskAssignmentId", query = "SELECT c FROM ContentApproval c WHERE c.taskAssignmentId = :taskAssignmentId")})
public class ContentApproval implements Serializable {
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
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Lob
    @Column(name = "note")
    private String note;
    @Column(name = "task_assignment_id")
    private Integer taskAssignmentId;

    public ContentApproval() {
    }

    public ContentApproval(Integer id) {
        this.id = id;
    }

    public ContentApproval(Integer id, int contentHeaderId, int userId, Date time) {
        this.id = id;
        this.contentHeaderId = contentHeaderId;
        this.userId = userId;
        this.time = time;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getTaskAssignmentId() {
        return taskAssignmentId;
    }

    public void setTaskAssignmentId(Integer taskAssignmentId) {
        this.taskAssignmentId = taskAssignmentId;
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
        if (!(object instanceof ContentApproval)) {
            return false;
        }
        ContentApproval other = (ContentApproval) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ContentApproval[ id=" + id + " ]";
    }
    
}
