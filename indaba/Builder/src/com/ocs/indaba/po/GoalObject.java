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
@Table(name = "goal_object")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GoalObject.findAll", query = "SELECT g FROM GoalObject g"),
    @NamedQuery(name = "GoalObject.findById", query = "SELECT g FROM GoalObject g WHERE g.id = :id"),
    @NamedQuery(name = "GoalObject.findByGoalId", query = "SELECT g FROM GoalObject g WHERE g.goalId = :goalId"),
    @NamedQuery(name = "GoalObject.findByEnterTime", query = "SELECT g FROM GoalObject g WHERE g.enterTime = :enterTime"),
    @NamedQuery(name = "GoalObject.findByExitTime", query = "SELECT g FROM GoalObject g WHERE g.exitTime = :exitTime"),
    @NamedQuery(name = "GoalObject.findByStatus", query = "SELECT g FROM GoalObject g WHERE g.status = :status"),
    @NamedQuery(name = "GoalObject.findBySequenceObjectId", query = "SELECT g FROM GoalObject g WHERE g.sequenceObjectId = :sequenceObjectId"),
    @NamedQuery(name = "GoalObject.findByEventLogId", query = "SELECT g FROM GoalObject g WHERE g.eventLogId = :eventLogId")})
public class GoalObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "goal_id")
    private int goalId;
    @Column(name = "enter_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enterTime;
    @Column(name = "exit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exitTime;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Basic(optional = false)
    @Column(name = "sequence_object_id")
    private int sequenceObjectId;
    @Column(name = "event_log_id")
    private Integer eventLogId;

    public GoalObject() {
    }

    public GoalObject(Integer id) {
        this.id = id;
    }

    public GoalObject(Integer id, int goalId, short status, int sequenceObjectId) {
        this.id = id;
        this.goalId = goalId;
        this.status = status;
        this.sequenceObjectId = sequenceObjectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public int getSequenceObjectId() {
        return sequenceObjectId;
    }

    public void setSequenceObjectId(int sequenceObjectId) {
        this.sequenceObjectId = sequenceObjectId;
    }

    public Integer getEventLogId() {
        return eventLogId;
    }

    public void setEventLogId(Integer eventLogId) {
        this.eventLogId = eventLogId;
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
        if (!(object instanceof GoalObject)) {
            return false;
        }
        GoalObject other = (GoalObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.GoalObject[ id=" + id + " ]";
    }
    
}
