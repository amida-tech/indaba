/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.po;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jiangjeff
 */
@Entity
@Table(name = "ws_indicator_instance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WsIndicatorInstance.findAll", query = "SELECT w FROM WsIndicatorInstance w"),
    @NamedQuery(name = "WsIndicatorInstance.findById", query = "SELECT w FROM WsIndicatorInstance w WHERE w.id = :id"),
    @NamedQuery(name = "WsIndicatorInstance.findByWorksetId", query = "SELECT w FROM WsIndicatorInstance w WHERE w.worksetId = :worksetId"),
    @NamedQuery(name = "WsIndicatorInstance.findByIndicatorId", query = "SELECT w FROM WsIndicatorInstance w WHERE w.indicatorId = :indicatorId"),
    @NamedQuery(name = "WsIndicatorInstance.findByOrgId", query = "SELECT w FROM WsIndicatorInstance w WHERE w.orgId = :orgId")})
public class WsIndicatorInstance implements Serializable {
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
    @Column(name = "indicator_id")
    private int indicatorId;
    @Basic(optional = false)
    @Column(name = "org_id")
    private int orgId;

    public WsIndicatorInstance() {
    }

    public WsIndicatorInstance(Integer id) {
        this.id = id;
    }

    public WsIndicatorInstance(Integer id, int worksetId, int indicatorId, int orgId) {
        this.id = id;
        this.worksetId = worksetId;
        this.indicatorId = indicatorId;
        this.orgId = orgId;
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

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
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
        if (!(object instanceof WsIndicatorInstance)) {
            return false;
        }
        WsIndicatorInstance other = (WsIndicatorInstance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.WsIndicatorInstance[ id=" + id + " ]";
    }
    
}
