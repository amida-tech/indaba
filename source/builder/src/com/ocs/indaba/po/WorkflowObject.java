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
@Table(name = "workflow_object")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkflowObject.findAll", query = "SELECT w FROM WorkflowObject w"),
    @NamedQuery(name = "WorkflowObject.findById", query = "SELECT w FROM WorkflowObject w WHERE w.id = :id"),
    @NamedQuery(name = "WorkflowObject.findByWorkflowId", query = "SELECT w FROM WorkflowObject w WHERE w.workflowId = :workflowId"),
    @NamedQuery(name = "WorkflowObject.findByStartTime", query = "SELECT w FROM WorkflowObject w WHERE w.startTime = :startTime"),
    @NamedQuery(name = "WorkflowObject.findByStatus", query = "SELECT w FROM WorkflowObject w WHERE w.status = :status"),
    @NamedQuery(name = "WorkflowObject.findByIsCancelled", query = "SELECT w FROM WorkflowObject w WHERE w.isCancelled = :isCancelled")})
public class WorkflowObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workflow_id")
    private int workflowId;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @Column(name = "is_cancelled")
    private boolean isCancelled;

    public WorkflowObject() {
    }

    public WorkflowObject(Integer id) {
        this.id = id;
    }

    public WorkflowObject(Integer id, int workflowId, int status, boolean isCancelled) {
        this.id = id;
        this.workflowId = workflowId;
        this.status = status;
        this.isCancelled = isCancelled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
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
        if (!(object instanceof WorkflowObject)) {
            return false;
        }
        WorkflowObject other = (WorkflowObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.WorkflowObject[ id=" + id + " ]";
    }
    
}
