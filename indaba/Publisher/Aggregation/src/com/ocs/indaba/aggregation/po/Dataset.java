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
@Table(name = "dataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dataset.findAll", query = "SELECT d FROM Dataset d"),
    @NamedQuery(name = "Dataset.findById", query = "SELECT d FROM Dataset d WHERE d.id = :id"),
    @NamedQuery(name = "Dataset.findByWorksetId", query = "SELECT d FROM Dataset d WHERE d.worksetId = :worksetId"),
    @NamedQuery(name = "Dataset.findByIncludesNonpubData", query = "SELECT d FROM Dataset d WHERE d.includesNonpubData = :includesNonpubData"),
    @NamedQuery(name = "Dataset.findByLastUpdateTime", query = "SELECT d FROM Dataset d WHERE d.lastUpdateTime = :lastUpdateTime")})
public class Dataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workset_id")
    private int worksetId;
    @Basic(optional = false)
    @Column(name = "includes_nonpub_data")
    private boolean includesNonpubData;
    @Basic(optional = false)
    @Column(name = "last_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public Dataset() {
    }

    public Dataset(Integer id) {
        this.id = id;
    }

    public Dataset(Integer id, int worksetId, boolean includesNonpubData, Date lastUpdateTime) {
        this.id = id;
        this.worksetId = worksetId;
        this.includesNonpubData = includesNonpubData;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorksetId() {
        return worksetId;
    }

    public void setWorksetId(int worksetId) {
        this.worksetId = worksetId;
    }

    public boolean getIncludesNonpubData() {
        return includesNonpubData;
    }

    public void setIncludesNonpubData(boolean includesNonpubData) {
        this.includesNonpubData = includesNonpubData;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
        if (!(object instanceof Dataset)) {
            return false;
        }
        Dataset other = (Dataset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.Dataset[ id=" + id + " ]";
    }
    
}
