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
@Table(name = "task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findById", query = "SELECT t FROM Task t WHERE t.id = :id"),
    @NamedQuery(name = "Task.findByTaskName", query = "SELECT t FROM Task t WHERE t.taskName = :taskName"),
    @NamedQuery(name = "Task.findByDescription", query = "SELECT t FROM Task t WHERE t.description = :description"),
    @NamedQuery(name = "Task.findByGoalId", query = "SELECT t FROM Task t WHERE t.goalId = :goalId"),
    @NamedQuery(name = "Task.findByProductId", query = "SELECT t FROM Task t WHERE t.productId = :productId"),
    @NamedQuery(name = "Task.findByToolId", query = "SELECT t FROM Task t WHERE t.toolId = :toolId"),
    @NamedQuery(name = "Task.findByAssignmentMethod", query = "SELECT t FROM Task t WHERE t.assignmentMethod = :assignmentMethod"),
    @NamedQuery(name = "Task.findByType", query = "SELECT t FROM Task t WHERE t.type = :type")})
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "task_name")
    private String taskName;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "goal_id")
    private int goalId;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;
    @Basic(optional = false)
    @Column(name = "tool_id")
    private int toolId;
    @Basic(optional = false)
    @Column(name = "assignment_method")
    private short assignmentMethod;
    @Basic(optional = false)
    @Lob
    @Column(name = "instructions")
    private String instructions;
    @Basic(optional = false)
    @Column(name = "type")
    private short type;
    @Column(name = "sticky")
    private short sticky;

    public Task() {
    }

    public Task(Integer id) {
        this.id = id;
    }

    public Task(Integer id, String taskName, int goalId, int productId, int toolId, short assignmentMethod, String instructions, short type) {
        this.id = id;
        this.taskName = taskName;
        this.goalId = goalId;
        this.productId = productId;
        this.toolId = toolId;
        this.assignmentMethod = assignmentMethod;
        this.instructions = instructions;
        this.type = type;
    }
    
    public Task(String name, String desc, int goalId, int productId, int toolId, short assignmentMethod, String instructions, short type)
    {
        this.taskName = name;
        this.description = desc;
        this.goalId = goalId;
        this.productId = productId;
        this.toolId = toolId;
        this.assignmentMethod = assignmentMethod;
        this.instructions = instructions;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

    public short getAssignmentMethod() {
        return assignmentMethod;
    }

    public void setAssignmentMethod(short assignmentMethod) {
        this.assignmentMethod = assignmentMethod;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getSticky() {
        return sticky;
    }

    public void setSticky(short sticky) {
        this.sticky = sticky;
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
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Task[id=" + id + ", name=" + taskName + ", goal=" + goalId + ", tool=" + toolId + ", sticky=" + sticky + " ]";
    }
    
}
