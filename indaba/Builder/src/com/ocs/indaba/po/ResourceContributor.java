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
@Table(name = "resource_contributor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResourceContributor.findAll", query = "SELECT r FROM ResourceContributor r"),
    @NamedQuery(name = "ResourceContributor.findById", query = "SELECT r FROM ResourceContributor r WHERE r.id = :id"),
    @NamedQuery(name = "ResourceContributor.findByResourceId", query = "SELECT r FROM ResourceContributor r WHERE r.resourceId = :resourceId"),
    @NamedQuery(name = "ResourceContributor.findByOrgId", query = "SELECT r FROM ResourceContributor r WHERE r.orgId = :orgId")})
public class ResourceContributor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "resource_id")
    private int resourceId;
    @Basic(optional = false)
    @Column(name = "org_id")
    private int orgId;

    public ResourceContributor() {
    }

    public ResourceContributor(Integer id) {
        this.id = id;
    }

    public ResourceContributor(Integer id, int resourceId, int orgId) {
        this.id = id;
        this.resourceId = resourceId;
        this.orgId = orgId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
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
        if (!(object instanceof ResourceContributor)) {
            return false;
        }
        ResourceContributor other = (ResourceContributor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ResourceContributor[ id=" + id + " ]";
    }
    
}
