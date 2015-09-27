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
@Table(name = "project_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectRoles.findAll", query = "SELECT p FROM ProjectRoles p"),
    @NamedQuery(name = "ProjectRoles.findById", query = "SELECT p FROM ProjectRoles p WHERE p.id = :id"),
    @NamedQuery(name = "ProjectRoles.findByProjectId", query = "SELECT p FROM ProjectRoles p WHERE p.projectId = :projectId"),
    @NamedQuery(name = "ProjectRoles.findByRoleId", query = "SELECT p FROM ProjectRoles p WHERE p.roleId = :roleId")})
public class ProjectRoles implements Serializable {
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
    @Column(name = "role_id")
    private int roleId;

    public ProjectRoles() {
    }

    public ProjectRoles(Integer id) {
        this.id = id;
    }

    public ProjectRoles(Integer id, int projectId, int roleId) {
        this.id = id;
        this.projectId = projectId;
        this.roleId = roleId;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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
        if (!(object instanceof ProjectRoles)) {
            return false;
        }
        ProjectRoles other = (ProjectRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ProjectRoles[ id=" + id + " ]";
    }
    
}
