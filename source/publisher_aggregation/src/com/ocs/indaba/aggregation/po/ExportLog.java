/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.po;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jiangjeff
 */
@Entity
@Table(name = "export_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExportLog.findAll", query = "SELECT e FROM ExportLog e"),
    @NamedQuery(name = "ExportLog.findById", query = "SELECT e FROM ExportLog e WHERE e.id = :id"),
    @NamedQuery(name = "ExportLog.findByTime", query = "SELECT e FROM ExportLog e WHERE e.time = :time"),
    @NamedQuery(name = "ExportLog.findByWorksetId", query = "SELECT e FROM ExportLog e WHERE e.worksetId = :worksetId"),
    @NamedQuery(name = "ExportLog.findByProductId", query = "SELECT e FROM ExportLog e WHERE e.productId = :productId"),
    @NamedQuery(name = "ExportLog.findByUserId", query = "SELECT e FROM ExportLog e WHERE e.userId = :userId")})
public class ExportLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Column(name = "workset_id")
    private Integer worksetId;
    @Column(name = "product_id")
    private Integer productId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;

    public ExportLog() {
    }

    public ExportLog(Integer id) {
        this.id = id;
    }

    public ExportLog(Integer id, Date time, int userId) {
        this.id = id;
        this.time = time;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getWorksetId() {
        return worksetId;
    }

    public void setWorksetId(Integer worksetId) {
        this.worksetId = worksetId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        if (!(object instanceof ExportLog)) {
            return false;
        }
        ExportLog other = (ExportLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.ExportLog[ id=" + id + " ]";
    }
    
}
