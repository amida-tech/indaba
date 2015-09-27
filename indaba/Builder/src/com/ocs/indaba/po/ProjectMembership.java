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
@Table(name = "project_membership")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectMembership.findAll", query = "SELECT p FROM ProjectMembership p"),
    @NamedQuery(name = "ProjectMembership.findById", query = "SELECT p FROM ProjectMembership p WHERE p.id = :id"),
    @NamedQuery(name = "ProjectMembership.findByUserId", query = "SELECT p FROM ProjectMembership p WHERE p.userId = :userId"),
    @NamedQuery(name = "ProjectMembership.findByRoleId", query = "SELECT p FROM ProjectMembership p WHERE p.roleId = :roleId"),
    @NamedQuery(name = "ProjectMembership.findByProjectId", query = "SELECT p FROM ProjectMembership p WHERE p.projectId = :projectId")})
public class ProjectMembership implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "role_id")
    private int roleId;
    @Basic(optional = false)
    @Column(name = "project_id")
    private int projectId;

    public ProjectMembership() {
    }

    public ProjectMembership(Integer id) {
        this.id = id;
    }

    public ProjectMembership(Integer id, int userId, int roleId, int projectId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
        this.projectId = projectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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
        if (!(object instanceof ProjectMembership)) {
            return false;
        }
        ProjectMembership other = (ProjectMembership) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ProjectMembership[ id=" + id + " ]";
    }
    
}
