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
@Table(name = "resource")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resource.findAll", query = "SELECT r FROM Resource r"),
    @NamedQuery(name = "Resource.findById", query = "SELECT r FROM Resource r WHERE r.id = :id"),
    @NamedQuery(name = "Resource.findByType", query = "SELECT r FROM Resource r WHERE r.type = :type"),
    @NamedQuery(name = "Resource.findByRid", query = "SELECT r FROM Resource r WHERE r.rid = :rid"),
    @NamedQuery(name = "Resource.findByCreatorOrgId", query = "SELECT r FROM Resource r WHERE r.creatorOrgId = :creatorOrgId"),
    @NamedQuery(name = "Resource.findByOwnerOrgId", query = "SELECT r FROM Resource r WHERE r.ownerOrgId = :ownerOrgId"),
    @NamedQuery(name = "Resource.findByVisibility", query = "SELECT r FROM Resource r WHERE r.visibility = :visibility"),
    @NamedQuery(name = "Resource.findByState", query = "SELECT r FROM Resource r WHERE r.state = :state"),
    @NamedQuery(name = "Resource.findByDefaultLanguageId", query = "SELECT r FROM Resource r WHERE r.defaultLanguageId = :defaultLanguageId"),
    @NamedQuery(name = "Resource.findByStatus", query = "SELECT r FROM Resource r WHERE r.status = :status"),
    @NamedQuery(name = "Resource.findByCreateTime", query = "SELECT r FROM Resource r WHERE r.createTime = :createTime"),
    @NamedQuery(name = "Resource.findByDeleteTime", query = "SELECT r FROM Resource r WHERE r.deleteTime = :deleteTime")})
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "type")
    private int type;
    @Basic(optional = false)
    @Column(name = "rid")
    private int rid;
    @Basic(optional = false)
    @Column(name = "creator_org_id")
    private int creatorOrgId;
    @Basic(optional = false)
    @Column(name = "owner_org_id")
    private int ownerOrgId;
    @Basic(optional = false)
    @Column(name = "visibility")
    private short visibility;
    @Basic(optional = false)
    @Column(name = "state")
    private short state;
    @Basic(optional = false)
    @Column(name = "default_language_id")
    private int defaultLanguageId;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "delete_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;

    public Resource() {
    }

    public Resource(Integer id) {
        this.id = id;
    }

    public Resource(Integer id, int type, int rid, int creatorOrgId, int ownerOrgId, short visibility, short state, int defaultLanguageId, short status, Date createTime) {
        this.id = id;
        this.type = type;
        this.rid = rid;
        this.creatorOrgId = creatorOrgId;
        this.ownerOrgId = ownerOrgId;
        this.visibility = visibility;
        this.state = state;
        this.defaultLanguageId = defaultLanguageId;
        this.status = status;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getCreatorOrgId() {
        return creatorOrgId;
    }

    public void setCreatorOrgId(int creatorOrgId) {
        this.creatorOrgId = creatorOrgId;
    }

    public int getOwnerOrgId() {
        return ownerOrgId;
    }

    public void setOwnerOrgId(int ownerOrgId) {
        this.ownerOrgId = ownerOrgId;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public int getDefaultLanguageId() {
        return defaultLanguageId;
    }

    public void setDefaultLanguageId(int defaultLanguageId) {
        this.defaultLanguageId = defaultLanguageId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
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
        if (!(object instanceof Resource)) {
            return false;
        }
        Resource other = (Resource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Resource[ id=" + id + " ]";
    }
    
}
