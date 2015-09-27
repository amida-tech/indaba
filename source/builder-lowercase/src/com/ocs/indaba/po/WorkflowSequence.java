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
@Table(name = "workflow_sequence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkflowSequence.findAll", query = "SELECT w FROM WorkflowSequence w"),
    @NamedQuery(name = "WorkflowSequence.findById", query = "SELECT w FROM WorkflowSequence w WHERE w.id = :id"),
    @NamedQuery(name = "WorkflowSequence.findByWorkflowId", query = "SELECT w FROM WorkflowSequence w WHERE w.workflowId = :workflowId"),
    @NamedQuery(name = "WorkflowSequence.findByName", query = "SELECT w FROM WorkflowSequence w WHERE w.name = :name"),
    @NamedQuery(name = "WorkflowSequence.findByDescription", query = "SELECT w FROM WorkflowSequence w WHERE w.description = :description")})
public class WorkflowSequence implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workflow_id")
    private int workflowId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    public WorkflowSequence() {
    }

    public WorkflowSequence(Integer id) {
        this.id = id;
    }

    public WorkflowSequence(Integer id, int workflowId, String name) {
        this.id = id;
        this.workflowId = workflowId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkflowSequence)) {
            return false;
        }
        WorkflowSequence other = (WorkflowSequence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.WorkflowSequence[ id=" + id + " ]";
    }
    
}
