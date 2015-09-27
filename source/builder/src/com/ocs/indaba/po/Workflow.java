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
@Table(name = "workflow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Workflow.findAll", query = "SELECT w FROM Workflow w"),
    @NamedQuery(name = "Workflow.findById", query = "SELECT w FROM Workflow w WHERE w.id = :id"),
    @NamedQuery(name = "Workflow.findByName", query = "SELECT w FROM Workflow w WHERE w.name = :name"),
    @NamedQuery(name = "Workflow.findByDescription", query = "SELECT w FROM Workflow w WHERE w.description = :description"),
    @NamedQuery(name = "Workflow.findByCreatedTime", query = "SELECT w FROM Workflow w WHERE w.createdTime = :createdTime"),
    @NamedQuery(name = "Workflow.findByCreatedByUserId", query = "SELECT w FROM Workflow w WHERE w.createdByUserId = :createdByUserId"),
    @NamedQuery(name = "Workflow.findByTotalDuration", query = "SELECT w FROM Workflow w WHERE w.totalDuration = :totalDuration")})
public class Workflow implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Basic(optional = false)
    @Column(name = "created_by_user_id")
    private int createdByUserId;
    @Column(name = "total_duration")
    private Integer totalDuration;

    public Workflow() {
    }

    public Workflow(Integer id) {
        this.id = id;
    }

    public Workflow(Integer id, String name, Date createdTime, int createdByUserId) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
        this.createdByUserId = createdByUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
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
        if (!(object instanceof Workflow)) {
            return false;
        }
        Workflow other = (Workflow) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Workflow[ id=" + id + " ]";
    }
    
}
