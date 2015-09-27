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
@Table(name = "ws_puser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WsPuser.findAll", query = "SELECT w FROM WsPuser w"),
    @NamedQuery(name = "WsPuser.findById", query = "SELECT w FROM WsPuser w WHERE w.id = :id"),
    @NamedQuery(name = "WsPuser.findByWorksetId", query = "SELECT w FROM WsPuser w WHERE w.worksetId = :worksetId"),
    @NamedQuery(name = "WsPuser.findByUserId", query = "SELECT w FROM WsPuser w WHERE w.userId = :userId")})
public class WsPuser implements Serializable {
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
    @Column(name = "user_id")
    private int userId;

    public WsPuser() {
    }

    public WsPuser(Integer id) {
        this.id = id;
    }

    public WsPuser(Integer id, int worksetId, int userId) {
        this.id = id;
        this.worksetId = worksetId;
        this.userId = userId;
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
        if (!(object instanceof WsPuser)) {
            return false;
        }
        WsPuser other = (WsPuser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.WsPuser[ id=" + id + " ]";
    }
    
}
