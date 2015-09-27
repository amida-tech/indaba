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
@Table(name = "tool")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tool.findAll", query = "SELECT t FROM Tool t"),
    @NamedQuery(name = "Tool.findById", query = "SELECT t FROM Tool t WHERE t.id = :id"),
    @NamedQuery(name = "Tool.findByName", query = "SELECT t FROM Tool t WHERE t.name = :name"),
    @NamedQuery(name = "Tool.findByLabel", query = "SELECT t FROM Tool t WHERE t.label = :label"),
    @NamedQuery(name = "Tool.findByDescription", query = "SELECT t FROM Tool t WHERE t.description = :description"),
    @NamedQuery(name = "Tool.findByAccessMatrixId", query = "SELECT t FROM Tool t WHERE t.accessMatrixId = :accessMatrixId"),
    @NamedQuery(name = "Tool.findByAction", query = "SELECT t FROM Tool t WHERE t.action = :action"),
    @NamedQuery(name = "Tool.findByTaskType", query = "SELECT t FROM Tool t WHERE t.taskType = :taskType"),
    @NamedQuery(name = "Tool.findByBscCompatible", query = "SELECT t FROM Tool t WHERE t.bscCompatible = :bscCompatible"),
    @NamedQuery(name = "Tool.findByMultiUser", query = "SELECT t FROM Tool t WHERE t.multiUser = :multiUser"),
    @NamedQuery(name = "Tool.findByContentType", query = "SELECT t FROM Tool t WHERE t.contentType = :contentType")})
public class Tool implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @Column(name = "description")
    private String description;
    @Column(name = "access_matrix_id")
    private Integer accessMatrixId;
    @Column(name = "action")
    private String action;
    @Basic(optional = false)
    @Column(name = "task_type")
    private short taskType;
    @Basic(optional = false)
    @Column(name = "bsc_compatible")
    private boolean bscCompatible;
    @Basic(optional = false)
    @Column(name = "multi_user")
    private boolean multiUser;
    @Lob
    @Column(name = "purpose")
    private String purpose;
    @Lob
    @Column(name = "inactive_reason")
    private String inactiveReason;
    @Basic(optional = false)
    @Column(name = "content_type")
    private short contentType;

    public Tool() {
    }

    public Tool(Integer id) {
        this.id = id;
    }

    public Tool(Integer id, String name, String label, short taskType, boolean bscCompatible, boolean multiUser, short contentType) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.taskType = taskType;
        this.bscCompatible = bscCompatible;
        this.multiUser = multiUser;
        this.contentType = contentType;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAccessMatrixId() {
        return accessMatrixId;
    }

    public void setAccessMatrixId(Integer accessMatrixId) {
        this.accessMatrixId = accessMatrixId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public short getTaskType() {
        return taskType;
    }

    public void setTaskType(short taskType) {
        this.taskType = taskType;
    }

    public boolean getBscCompatible() {
        return bscCompatible;
    }

    public void setBscCompatible(boolean bscCompatible) {
        this.bscCompatible = bscCompatible;
    }

    public boolean getMultiUser() {
        return multiUser;
    }

    public void setMultiUser(boolean multiUser) {
        this.multiUser = multiUser;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getInactiveReason() {
        return inactiveReason;
    }

    public void setInactiveReason(String inactiveReason) {
        this.inactiveReason = inactiveReason;
    }

    public short getContentType() {
        return contentType;
    }

    public void setContentType(short contentType) {
        this.contentType = contentType;
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
        if (!(object instanceof Tool)) {
            return false;
        }
        Tool other = (Tool) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Tool[ id=" + id + " ]";
    }
    
}
