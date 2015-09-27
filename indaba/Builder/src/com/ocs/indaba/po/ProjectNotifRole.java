/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

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

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "project_notif_role")
@NamedQueries({
    @NamedQuery(name = "ProjectNotifRole.findAll", query = "SELECT p FROM ProjectNotifRole p"),
    @NamedQuery(name = "ProjectNotifRole.findById", query = "SELECT p FROM ProjectNotifRole p WHERE p.id = :id"),
    @NamedQuery(name = "ProjectNotifRole.findByRoleId", query = "SELECT p FROM ProjectNotifRole p WHERE p.roleId = :roleId"),
    @NamedQuery(name = "ProjectNotifRole.findByProjectNotifId", query = "SELECT p FROM ProjectNotifRole p WHERE p.projectNotifId = :projectNotifId")})
public class ProjectNotifRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "project_notif_id")
    private Integer projectNotifId;

    public ProjectNotifRole() {
    }

    public ProjectNotifRole(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getProjectNotifId() {
        return projectNotifId;
    }

    public void setProjectNotifId(Integer projectNotifId) {
        this.projectNotifId = projectNotifId;
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
        if (!(object instanceof ProjectNotifRole)) {
            return false;
        }
        ProjectNotifRole other = (ProjectNotifRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ProjectNotifRole[id=" + id + "]";
    }

}
