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
@Table(name = "project_contact")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectContact.findAll", query = "SELECT p FROM ProjectContact p"),
    @NamedQuery(name = "ProjectContact.findById", query = "SELECT p FROM ProjectContact p WHERE p.id = :id"),
    @NamedQuery(name = "ProjectContact.findByProjectId", query = "SELECT p FROM ProjectContact p WHERE p.projectId = :projectId"),
    @NamedQuery(name = "ProjectContact.findByUserId", query = "SELECT p FROM ProjectContact p WHERE p.userId = :userId")})
public class ProjectContact implements Serializable {
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
    @Column(name = "user_id")
    private int userId;

    public ProjectContact() {
    }

    public ProjectContact(Integer id) {
        this.id = id;
    }

    public ProjectContact(Integer id, int projectId, int userId) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
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
        if (!(object instanceof ProjectContact)) {
            return false;
        }
        ProjectContact other = (ProjectContact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ProjectContact[ id=" + id + " ]";
    }
    
}
