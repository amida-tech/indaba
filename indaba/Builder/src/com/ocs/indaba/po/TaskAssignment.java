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
@Table(name = "task_assignment")
@NamedQueries({
    @NamedQuery(name = "TaskAssignment.findAll", query = "SELECT t FROM TaskAssignment t"),
    @NamedQuery(name = "TaskAssignment.findById", query = "SELECT t FROM TaskAssignment t WHERE t.id = :id"),
    @NamedQuery(name = "TaskAssignment.findByTaskId", query = "SELECT t FROM TaskAssignment t WHERE t.taskId = :taskId"),
    @NamedQuery(name = "TaskAssignment.findByTargetId", query = "SELECT t FROM TaskAssignment t WHERE t.targetId = :targetId"),
    @NamedQuery(name = "TaskAssignment.findByAssignedUserId", query = "SELECT t FROM TaskAssignment t WHERE t.assignedUserId = :assignedUserId"),
    @NamedQuery(name = "TaskAssignment.findByDueTime", query = "SELECT t FROM TaskAssignment t WHERE t.dueTime = :dueTime"),
    @NamedQuery(name = "TaskAssignment.findByStatus", query = "SELECT t FROM TaskAssignment t WHERE t.status = :status"),
    @NamedQuery(name = "TaskAssignment.findByStartTime", query = "SELECT t FROM TaskAssignment t WHERE t.startTime = :startTime"),
    @NamedQuery(name = "TaskAssignment.findByCompletionTime", query = "SELECT t FROM TaskAssignment t WHERE t.completionTime = :completionTime"),
    @NamedQuery(name = "TaskAssignment.findByEventLogId", query = "SELECT t FROM TaskAssignment t WHERE t.eventLogId = :eventLogId"),
    @NamedQuery(name = "TaskAssignment.findByQEnterTime", query = "SELECT t FROM TaskAssignment t WHERE t.qEnterTime = :qEnterTime"),
    @NamedQuery(name = "TaskAssignment.findByQLastAssignedTime", query = "SELECT t FROM TaskAssignment t WHERE t.qLastAssignedTime = :qLastAssignedTime"),
    @NamedQuery(name = "TaskAssignment.findByQLastAssignedUid", query = "SELECT t FROM TaskAssignment t WHERE t.qLastAssignedUid = :qLastAssignedUid"),
    @NamedQuery(name = "TaskAssignment.findByQLastReturnTime", query = "SELECT t FROM TaskAssignment t WHERE t.qLastReturnTime = :qLastReturnTime"),
    @NamedQuery(name = "TaskAssignment.findByQPriority", query = "SELECT t FROM TaskAssignment t WHERE t.qPriority = :qPriority"),
    @NamedQuery(name = "TaskAssignment.findByGoalObjectId", query = "SELECT t FROM TaskAssignment t WHERE t.goalObjectId = :goalObjectId"),
    @NamedQuery(name = "TaskAssignment.findByPercent", query = "SELECT t FROM TaskAssignment t WHERE t.percent = :percent"),
    @NamedQuery(name = "TaskAssignment.findByHorseId", query = "SELECT t FROM TaskAssignment t WHERE t.horseId = :horseId"),
    @NamedQuery(name = "TaskAssignment.findByExitType", query = "SELECT t FROM TaskAssignment t WHERE t.exitType = :exitType")})
public class TaskAssignment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "task_id")
    private int taskId;
    @Basic(optional = false)
    @Column(name = "target_id")
    private int targetId;
    @Basic(optional = false)
    @Column(name = "assigned_user_id")
    private int assignedUserId;
    @Column(name = "due_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueTime;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "completion_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionTime;
    @Column(name = "event_log_id")
    private Integer eventLogId;
    @Column(name = "q_enter_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date qEnterTime;
    @Column(name = "q_last_assigned_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date qLastAssignedTime;
    @Column(name = "q_last_assigned_uid")
    private Integer qLastAssignedUid;
    @Column(name = "q_last_return_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date qLastReturnTime;
    @Column(name = "q_priority")
    private Short qPriority;
    @Lob
    @Column(name = "data")
    private String data;
    @Column(name = "goal_object_id")
    private Integer goalObjectId;
    @Column(name = "percent")
    private Float percent;
    @Basic(optional = false)
    @Column(name = "horse_id")
    private int horseId;
    @Basic(optional = false)
    @Column(name = "exit_type")
    private short exitType;
    @Lob
    @Column(name = "status_data")
    private String statusData;

    public TaskAssignment() {
    }

    public TaskAssignment(Integer id) {
        this.id = id;
    }

    public TaskAssignment(Integer id, int taskId, int targetId, int assignedUserId, short status, int horseId, short exitType) {
        this.id = id;
        this.taskId = taskId;
        this.targetId = targetId;
        this.assignedUserId = assignedUserId;
        this.status = status;
        this.horseId = horseId;
        this.exitType = exitType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
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

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    public Integer getEventLogId() {
        return eventLogId;
    }

    public void setEventLogId(Integer eventLogId) {
        this.eventLogId = eventLogId;
    }

    public Date getQEnterTime() {
        return qEnterTime;
    }

    public void setQEnterTime(Date qEnterTime) {
        this.qEnterTime = qEnterTime;
    }

    public Date getQLastAssignedTime() {
        return qLastAssignedTime;
    }

    public void setQLastAssignedTime(Date qLastAssignedTime) {
        this.qLastAssignedTime = qLastAssignedTime;
    }

    public Integer getQLastAssignedUid() {
        return qLastAssignedUid;
    }

    public void setQLastAssignedUid(Integer qLastAssignedUid) {
        this.qLastAssignedUid = qLastAssignedUid;
    }

    public Date getQLastReturnTime() {
        return qLastReturnTime;
    }

    public void setQLastReturnTime(Date qLastReturnTime) {
        this.qLastReturnTime = qLastReturnTime;
    }

    public Short getQPriority() {
        return qPriority;
    }

    public void setQPriority(Short qPriority) {
        this.qPriority = qPriority;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getGoalObjectId() {
        return goalObjectId;
    }

    public void setGoalObjectId(Integer goalObjectId) {
        this.goalObjectId = goalObjectId;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public short getExitType() {
        return exitType;
    }

    public void setExitType(short exitType) {
        this.exitType = exitType;
    }

    public String getStatusData() {
        return statusData;
    }

    public void setStatusData(String statusData) {
        this.statusData = statusData;
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
        if (!(object instanceof TaskAssignment)) {
            return false;
        }
        TaskAssignment other = (TaskAssignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.TaskAssignment[id=" + id + "]";
    }

}
