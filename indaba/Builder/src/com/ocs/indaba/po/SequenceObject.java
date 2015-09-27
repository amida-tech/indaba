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
@Table(name = "sequence_object")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SequenceObject.findAll", query = "SELECT s FROM SequenceObject s"),
    @NamedQuery(name = "SequenceObject.findById", query = "SELECT s FROM SequenceObject s WHERE s.id = :id"),
    @NamedQuery(name = "SequenceObject.findByWorkflowObjectId", query = "SELECT s FROM SequenceObject s WHERE s.workflowObjectId = :workflowObjectId"),
    @NamedQuery(name = "SequenceObject.findByWorkflowSequenceId", query = "SELECT s FROM SequenceObject s WHERE s.workflowSequenceId = :workflowSequenceId"),
    @NamedQuery(name = "SequenceObject.findByStatus", query = "SELECT s FROM SequenceObject s WHERE s.status = :status")})
public class SequenceObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workflow_object_id")
    private int workflowObjectId;
    @Basic(optional = false)
    @Column(name = "workflow_sequence_id")
    private int workflowSequenceId;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;

    public SequenceObject() {
    }

    public SequenceObject(Integer id) {
        this.id = id;
    }

    public SequenceObject(Integer id, int workflowObjectId, int workflowSequenceId, short status) {
        this.id = id;
        this.workflowObjectId = workflowObjectId;
        this.workflowSequenceId = workflowSequenceId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorkflowObjectId() {
        return workflowObjectId;
    }

    public void setWorkflowObjectId(int workflowObjectId) {
        this.workflowObjectId = workflowObjectId;
    }

    public int getWorkflowSequenceId() {
        return workflowSequenceId;
    }

    public void setWorkflowSequenceId(int workflowSequenceId) {
        this.workflowSequenceId = workflowSequenceId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
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
        if (!(object instanceof SequenceObject)) {
            return false;
        }
        SequenceObject other = (SequenceObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SequenceObject[ id=" + id + " ]";
    }
    
}
