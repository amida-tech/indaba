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
@Table(name = "project_target")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectTarget.findAll", query = "SELECT p FROM ProjectTarget p"),
    @NamedQuery(name = "ProjectTarget.findById", query = "SELECT p FROM ProjectTarget p WHERE p.id = :id"),
    @NamedQuery(name = "ProjectTarget.findByProjectId", query = "SELECT p FROM ProjectTarget p WHERE p.projectId = :projectId"),
    @NamedQuery(name = "ProjectTarget.findByTargetId", query = "SELECT p FROM ProjectTarget p WHERE p.targetId = :targetId")})
public class ProjectTarget implements Serializable {
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
    @Column(name = "target_id")
    private int targetId;

    public ProjectTarget() {
    }

    public ProjectTarget(Integer id) {
        this.id = id;
    }

    public ProjectTarget(Integer id, int projectId, int targetId) {
        this.id = id;
        this.projectId = projectId;
        this.targetId = targetId;
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

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
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
        if (!(object instanceof ProjectTarget)) {
            return false;
        }
        ProjectTarget other = (ProjectTarget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ProjectTarget[ id=" + id + " ]";
    }
    
}
