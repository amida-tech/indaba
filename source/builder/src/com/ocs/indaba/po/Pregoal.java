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
@Table(name = "pregoal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pregoal.findAll", query = "SELECT p FROM Pregoal p"),
    @NamedQuery(name = "Pregoal.findById", query = "SELECT p FROM Pregoal p WHERE p.id = :id"),
    @NamedQuery(name = "Pregoal.findByWorkflowSequenceId", query = "SELECT p FROM Pregoal p WHERE p.workflowSequenceId = :workflowSequenceId"),
    @NamedQuery(name = "Pregoal.findByPreGoalId", query = "SELECT p FROM Pregoal p WHERE p.preGoalId = :preGoalId")})
public class Pregoal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workflow_sequence_id")
    private int workflowSequenceId;
    @Basic(optional = false)
    @Column(name = "pre_goal_id")
    private int preGoalId;

    public Pregoal() {
    }

    public Pregoal(Integer id) {
        this.id = id;
    }

    public Pregoal(Integer id, int workflowSequenceId, int preGoalId) {
        this.id = id;
        this.workflowSequenceId = workflowSequenceId;
        this.preGoalId = preGoalId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorkflowSequenceId() {
        return workflowSequenceId;
    }

    public void setWorkflowSequenceId(int workflowSequenceId) {
        this.workflowSequenceId = workflowSequenceId;
    }

    public int getPreGoalId() {
        return preGoalId;
    }

    public void setPreGoalId(int preGoalId) {
        this.preGoalId = preGoalId;
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
        if (!(object instanceof Pregoal)) {
            return false;
        }
        Pregoal other = (Pregoal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Pregoal[ id=" + id + " ]";
    }
    
}
