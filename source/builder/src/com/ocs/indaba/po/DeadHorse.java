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
@Table(name = "dead_horse")
@NamedQueries({
    @NamedQuery(name = "DeadHorse.findAll", query = "SELECT d FROM DeadHorse d"),
    @NamedQuery(name = "DeadHorse.findById", query = "SELECT d FROM DeadHorse d WHERE d.id = :id"),
    @NamedQuery(name = "DeadHorse.findByProductId", query = "SELECT d FROM DeadHorse d WHERE d.productId = :productId"),
    @NamedQuery(name = "DeadHorse.findByTargetId", query = "SELECT d FROM DeadHorse d WHERE d.targetId = :targetId"),
    @NamedQuery(name = "DeadHorse.findByStartTime", query = "SELECT d FROM DeadHorse d WHERE d.startTime = :startTime"),
    @NamedQuery(name = "DeadHorse.findByCompletionTime", query = "SELECT d FROM DeadHorse d WHERE d.completionTime = :completionTime"),
    @NamedQuery(name = "DeadHorse.findByContentHeaderId", query = "SELECT d FROM DeadHorse d WHERE d.contentHeaderId = :contentHeaderId"),
    @NamedQuery(name = "DeadHorse.findByWorkflowObjectId", query = "SELECT d FROM DeadHorse d WHERE d.workflowObjectId = :workflowObjectId"),
    @NamedQuery(name = "DeadHorse.findByKillTime", query = "SELECT d FROM DeadHorse d WHERE d.killTime = :killTime")})
public class DeadHorse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;
    @Basic(optional = false)
    @Column(name = "target_id")
    private int targetId;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "completion_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionTime;
    @Basic(optional = false)
    @Column(name = "content_header_id")
    private int contentHeaderId;
    @Basic(optional = false)
    @Column(name = "workflow_object_id")
    private int workflowObjectId;
    @Basic(optional = false)
    @Column(name = "kill_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date killTime;

    public DeadHorse() {
    }

    public DeadHorse(Integer id) {
        this.id = id;
    }

    public DeadHorse(Integer id, int productId, int targetId, int contentHeaderId, int workflowObjectId, Date killTime) {
        this.id = id;
        this.productId = productId;
        this.targetId = targetId;
        this.contentHeaderId = contentHeaderId;
        this.workflowObjectId = workflowObjectId;
        this.killTime = killTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
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

    public int getContentHeaderId() {
        return contentHeaderId;
    }

    public void setContentHeaderId(int contentHeaderId) {
        this.contentHeaderId = contentHeaderId;
    }

    public int getWorkflowObjectId() {
        return workflowObjectId;
    }

    public void setWorkflowObjectId(int workflowObjectId) {
        this.workflowObjectId = workflowObjectId;
    }

    public Date getKillTime() {
        return killTime;
    }

    public void setKillTime(Date killTime) {
        this.killTime = killTime;
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
        if (!(object instanceof DeadHorse)) {
            return false;
        }
        DeadHorse other = (DeadHorse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.DeadHorse[id=" + id + "]";
    }

}
