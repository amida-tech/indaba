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
@Table(name = "ws_project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WsProject.findAll", query = "SELECT w FROM WsProject w"),
    @NamedQuery(name = "WsProject.findById", query = "SELECT w FROM WsProject w WHERE w.id = :id"),
    @NamedQuery(name = "WsProject.findByWorksetId", query = "SELECT w FROM WsProject w WHERE w.worksetId = :worksetId"),
    @NamedQuery(name = "WsProject.findByProjectId", query = "SELECT w FROM WsProject w WHERE w.projectId = :projectId")})
public class WsProject implements Serializable {
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
    @Column(name = "project_id")
    private int projectId;

    public WsProject() {
    }

    public WsProject(Integer id) {
        this.id = id;
    }

    public WsProject(Integer id, int worksetId, int projectId) {
        this.id = id;
        this.worksetId = worksetId;
        this.projectId = projectId;
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
        if (!(object instanceof WsProject)) {
            return false;
        }
        WsProject other = (WsProject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.WsProject[ id=" + id + " ]";
    }
    
}
