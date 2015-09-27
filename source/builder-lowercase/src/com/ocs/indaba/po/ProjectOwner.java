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
@Table(name = "project_owner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectOwner.findAll", query = "SELECT p FROM ProjectOwner p"),
    @NamedQuery(name = "ProjectOwner.findById", query = "SELECT p FROM ProjectOwner p WHERE p.id = :id"),
    @NamedQuery(name = "ProjectOwner.findByProjectId", query = "SELECT p FROM ProjectOwner p WHERE p.projectId = :projectId"),
    @NamedQuery(name = "ProjectOwner.findByOrgId", query = "SELECT p FROM ProjectOwner p WHERE p.orgId = :orgId")})
public class ProjectOwner implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "project_id")
    private int projectId;
    @Basic(optional = false)
    @Column(name = "org_id")
    private int orgId;

    public ProjectOwner() {
    }

    public ProjectOwner(Integer id) {
        this.id = id;
    }

    public ProjectOwner(Integer id, int projectId, int orgId) {
        this.id = id;
        this.projectId = projectId;
        this.orgId = orgId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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
        if (!(object instanceof ProjectOwner)) {
            return false;
        }
        ProjectOwner other = (ProjectOwner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ProjectOwner[ id=" + id + " ]";
    }
    
}
