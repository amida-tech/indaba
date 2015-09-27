/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "tasksub")
@NamedQueries({
    @NamedQuery(name = "Tasksub.findAll", query = "SELECT t FROM Tasksub t"),
    @NamedQuery(name = "Tasksub.findById", query = "SELECT t FROM Tasksub t WHERE t.id = :id"),
    @NamedQuery(name = "Tasksub.findByUserId", query = "SELECT t FROM Tasksub t WHERE t.userId = :userId"),
    @NamedQuery(name = "Tasksub.findByTaskId", query = "SELECT t FROM Tasksub t WHERE t.taskId = :taskId"),
    @NamedQuery(name = "Tasksub.findByNotify", query = "SELECT t FROM Tasksub t WHERE t.notify = :notify")})
public class Tasksub implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "task_id")
    private int taskId;
    @Basic(optional = false)
    @Column(name = "notify")
    private boolean notify;

    public Tasksub() {
    }

    public Tasksub(Integer id) {
        this.id = id;
    }

    public Tasksub(Integer id, int userId, int taskId, boolean notify) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.notify = notify;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean getNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
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
        if (!(object instanceof Tasksub)) {
            return false;
        }
        Tasksub other = (Tasksub) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Tasksub[id=" + id + "]";
    }

}
