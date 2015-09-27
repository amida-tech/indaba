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
@Table(name = "cases")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cases.findAll", query = "SELECT c FROM Cases c"),
    @NamedQuery(name = "Cases.findById", query = "SELECT c FROM Cases c WHERE c.id = :id"),
    @NamedQuery(name = "Cases.findByOpenedByUserId", query = "SELECT c FROM Cases c WHERE c.openedByUserId = :openedByUserId"),
    @NamedQuery(name = "Cases.findByAssignedUserId", query = "SELECT c FROM Cases c WHERE c.assignedUserId = :assignedUserId"),
    @NamedQuery(name = "Cases.findByOpenedTime", query = "SELECT c FROM Cases c WHERE c.openedTime = :openedTime"),
    @NamedQuery(name = "Cases.findByTitle", query = "SELECT c FROM Cases c WHERE c.title = :title"),
    @NamedQuery(name = "Cases.findByPriority", query = "SELECT c FROM Cases c WHERE c.priority = :priority"),
    @NamedQuery(name = "Cases.findByStatus", query = "SELECT c FROM Cases c WHERE c.status = :status"),
    @NamedQuery(name = "Cases.findBySubstatus", query = "SELECT c FROM Cases c WHERE c.substatus = :substatus"),
    @NamedQuery(name = "Cases.findByBlockWorkflow", query = "SELECT c FROM Cases c WHERE c.blockWorkflow = :blockWorkflow"),
    @NamedQuery(name = "Cases.findByBlockPublishing", query = "SELECT c FROM Cases c WHERE c.blockPublishing = :blockPublishing"),
    @NamedQuery(name = "Cases.findByProjectId", query = "SELECT c FROM Cases c WHERE c.projectId = :projectId"),
    @NamedQuery(name = "Cases.findByProductId", query = "SELECT c FROM Cases c WHERE c.productId = :productId"),
    @NamedQuery(name = "Cases.findByHorseId", query = "SELECT c FROM Cases c WHERE c.horseId = :horseId"),
    @NamedQuery(name = "Cases.findByGoalId", query = "SELECT c FROM Cases c WHERE c.goalId = :goalId"),
    @NamedQuery(name = "Cases.findByStaffMsgboardId", query = "SELECT c FROM Cases c WHERE c.staffMsgboardId = :staffMsgboardId"),
    @NamedQuery(name = "Cases.findByUserMsgboardId", query = "SELECT c FROM Cases c WHERE c.userMsgboardId = :userMsgboardId"),
    @NamedQuery(name = "Cases.findByLastUpdatedTime", query = "SELECT c FROM Cases c WHERE c.lastUpdatedTime = :lastUpdatedTime")})
public class Cases implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "opened_by_user_id")
    private int openedByUserId;
    @Column(name = "assigned_user_id")
    private Integer assignedUserId;
    @Basic(optional = false)
    @Column(name = "opened_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date openedTime;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "priority")
    private short priority;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "substatus")
    private Short substatus;
    @Column(name = "block_workflow")
    private Boolean blockWorkflow;
    @Column(name = "block_publishing")
    private Boolean blockPublishing;
    @Column(name = "project_id")
    private Integer projectId;
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "horse_id")
    private Integer horseId;
    @Column(name = "goal_id")
    private Integer goalId;
    @Column(name = "staff_msgboard_id")
    private Integer staffMsgboardId;
    @Column(name = "user_msgboard_id")
    private Integer userMsgboardId;
    @Column(name = "last_updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    public Cases() {
    }

    public Cases(Integer id) {
        this.id = id;
    }

    public Cases(Integer id, int openedByUserId, Date openedTime, String title, short priority, short status) {
        this.id = id;
        this.openedByUserId = openedByUserId;
        this.openedTime = openedTime;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOpenedByUserId() {
        return openedByUserId;
    }

    public void setOpenedByUserId(int openedByUserId) {
        this.openedByUserId = openedByUserId;
    }

    public Integer getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Integer assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public Date getOpenedTime() {
        return openedTime;
    }

    public void setOpenedTime(Date openedTime) {
        this.openedTime = openedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Short getSubstatus() {
        return substatus;
    }

    public void setSubstatus(Short substatus) {
        this.substatus = substatus;
    }

    public Boolean getBlockWorkflow() {
        return blockWorkflow;
    }

    public void setBlockWorkflow(Boolean blockWorkflow) {
        this.blockWorkflow = blockWorkflow;
    }

    public Boolean getBlockPublishing() {
        return blockPublishing;
    }

    public void setBlockPublishing(Boolean blockPublishing) {
        this.blockPublishing = blockPublishing;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getHorseId() {
        return horseId;
    }

    public void setHorseId(Integer horseId) {
        this.horseId = horseId;
    }

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    public Integer getStaffMsgboardId() {
        return staffMsgboardId;
    }

    public void setStaffMsgboardId(Integer staffMsgboardId) {
        this.staffMsgboardId = staffMsgboardId;
    }

    public Integer getUserMsgboardId() {
        return userMsgboardId;
    }

    public void setUserMsgboardId(Integer userMsgboardId) {
        this.userMsgboardId = userMsgboardId;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
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
        if (!(object instanceof Cases)) {
            return false;
        }
        Cases other = (Cases) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Cases[ id=" + id + " ]";
    }
    
}
