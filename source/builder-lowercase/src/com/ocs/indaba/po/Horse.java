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
@Table(name = "horse")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Horse.findAll", query = "SELECT h FROM Horse h"),
    @NamedQuery(name = "Horse.findById", query = "SELECT h FROM Horse h WHERE h.id = :id"),
    @NamedQuery(name = "Horse.findByProductId", query = "SELECT h FROM Horse h WHERE h.productId = :productId"),
    @NamedQuery(name = "Horse.findByTargetId", query = "SELECT h FROM Horse h WHERE h.targetId = :targetId"),
    @NamedQuery(name = "Horse.findByStartTime", query = "SELECT h FROM Horse h WHERE h.startTime = :startTime"),
    @NamedQuery(name = "Horse.findByCompletionTime", query = "SELECT h FROM Horse h WHERE h.completionTime = :completionTime"),
    @NamedQuery(name = "Horse.findByContentHeaderId", query = "SELECT h FROM Horse h WHERE h.contentHeaderId = :contentHeaderId"),
    @NamedQuery(name = "Horse.findByWorkflowObjectId", query = "SELECT h FROM Horse h WHERE h.workflowObjectId = :workflowObjectId")})
public class Horse implements Serializable {
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

    public Horse() {
    }

    public Horse(Integer id) {
        this.id = id;
    }

    public Horse(Integer id, int productId, int targetId, int contentHeaderId, int workflowObjectId) {
        this.id = id;
        this.productId = productId;
        this.targetId = targetId;
        this.contentHeaderId = contentHeaderId;
        this.workflowObjectId = workflowObjectId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horse)) {
            return false;
        }
        Horse other = (Horse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Horse[ id=" + id + " ]";
    }
    
}
