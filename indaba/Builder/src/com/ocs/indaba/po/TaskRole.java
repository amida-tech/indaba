/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.po;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "task_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaskRole.findAll", query = "SELECT t FROM TaskRole t"),
    @NamedQuery(name = "TaskRole.findById", query = "SELECT t FROM TaskRole t WHERE t.id = :id"),
    @NamedQuery(name = "TaskRole.findByTaskId", query = "SELECT t FROM TaskRole t WHERE t.taskId = :taskId"),
    @NamedQuery(name = "TaskRole.findByRoleId", query = "SELECT t FROM TaskRole t WHERE t.roleId = :roleId"),
    @NamedQuery(name = "TaskRole.findByCanClaim", query = "SELECT t FROM TaskRole t WHERE t.canClaim = :canClaim")})
public class TaskRole implements Serializable {
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
    @Column(name = "role_id")
    private int roleId;
    @Basic(optional = false)
    @Column(name = "can_claim")
    private short canClaim;

    public TaskRole() {
    }

    public TaskRole(Integer id) {
        this.id = id;
    }

    public TaskRole(Integer id, int taskId, int roleId, short canClaim) {
        this.id = id;
        this.taskId = taskId;
        this.roleId = roleId;
        this.canClaim = canClaim;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public short getCanClaim() {
        return canClaim;
    }

    public void setCanClaim(short canClaim) {
        this.canClaim = canClaim;
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
        if (!(object instanceof TaskRole)) {
            return false;
        }
        TaskRole other = (TaskRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.TaskRole[ id=" + id + " ]";
    }
    
}
