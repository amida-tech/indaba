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

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "workset")
@NamedQueries({
    @NamedQuery(name = "Workset.findAll", query = "SELECT w FROM Workset w"),
    @NamedQuery(name = "Workset.findById", query = "SELECT w FROM Workset w WHERE w.id = :id"),
    @NamedQuery(name = "Workset.findByName", query = "SELECT w FROM Workset w WHERE w.name = :name"),
    @NamedQuery(name = "Workset.findByDescription", query = "SELECT w FROM Workset w WHERE w.description = :description"),
    @NamedQuery(name = "Workset.findByDefinedByUserId", query = "SELECT w FROM Workset w WHERE w.definedByUserId = :definedByUserId"),
    @NamedQuery(name = "Workset.findByDefinedTime", query = "SELECT w FROM Workset w WHERE w.definedTime = :definedTime"),
    @NamedQuery(name = "Workset.findByOrgId", query = "SELECT w FROM Workset w WHERE w.orgId = :orgId"),
    @NamedQuery(name = "Workset.findByVisibility", query = "SELECT w FROM Workset w WHERE w.visibility = :visibility"),
    @NamedQuery(name = "Workset.findByIsActive", query = "SELECT w FROM Workset w WHERE w.isActive = :isActive"),
    @NamedQuery(name = "Workset.findByLastUpdateTime", query = "SELECT w FROM Workset w WHERE w.lastUpdateTime = :lastUpdateTime")})
public class Workset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "defined_by_user_id")
    private int definedByUserId;
    @Basic(optional = false)
    @Column(name = "defined_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date definedTime;
    @Basic(optional = false)
    @Column(name = "org_id")
    private int orgId;
    @Basic(optional = false)
    @Column(name = "visibility")
    private short visibility;
    @Basic(optional = false)
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @Column(name = "last_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public Workset() {
    }

    public Workset(Integer id) {
        this.id = id;
    }

    public Workset(Integer id, String name, int definedByUserId, Date definedTime, int orgId, short visibility, boolean isActive, Date lastUpdateTime) {
        this.id = id;
        this.name = name;
        this.definedByUserId = definedByUserId;
        this.definedTime = definedTime;
        this.orgId = orgId;
        this.visibility = visibility;
        this.isActive = isActive;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefinedByUserId() {
        return definedByUserId;
    }

    public void setDefinedByUserId(int definedByUserId) {
        this.definedByUserId = definedByUserId;
    }

    public Date getDefinedTime() {
        return definedTime;
    }

    public void setDefinedTime(Date definedTime) {
        this.definedTime = definedTime;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
        if (!(object instanceof Workset)) {
            return false;
        }
        Workset other = (Workset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.Workset[id=" + id + "]";
    }

}
