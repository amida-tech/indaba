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
@Table(name = "userfinder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Userfinder.findAll", query = "SELECT u FROM Userfinder u"),
    @NamedQuery(name = "Userfinder.findById", query = "SELECT u FROM Userfinder u WHERE u.id = :id"),
    @NamedQuery(name = "Userfinder.findByDescription", query = "SELECT u FROM Userfinder u WHERE u.description = :description"),
    @NamedQuery(name = "Userfinder.findByProjectId", query = "SELECT u FROM Userfinder u WHERE u.projectId = :projectId"),
    @NamedQuery(name = "Userfinder.findByRoleId", query = "SELECT u FROM Userfinder u WHERE u.roleId = :roleId"),
    @NamedQuery(name = "Userfinder.findByAssignedUserId", query = "SELECT u FROM Userfinder u WHERE u.assignedUserId = :assignedUserId"),
    @NamedQuery(name = "Userfinder.findByCasePriority", query = "SELECT u FROM Userfinder u WHERE u.casePriority = :casePriority"),
    @NamedQuery(name = "Userfinder.findByAttachUser", query = "SELECT u FROM Userfinder u WHERE u.attachUser = :attachUser"),
    @NamedQuery(name = "Userfinder.findByAttachContent", query = "SELECT u FROM Userfinder u WHERE u.attachContent = :attachContent"),
    @NamedQuery(name = "Userfinder.findByStatus", query = "SELECT u FROM Userfinder u WHERE u.status = :status"),
    @NamedQuery(name = "Userfinder.findByCreateTime", query = "SELECT u FROM Userfinder u WHERE u.createTime = :createTime"),
    @NamedQuery(name = "Userfinder.findByLastUpdateTime", query = "SELECT u FROM Userfinder u WHERE u.lastUpdateTime = :lastUpdateTime"),
    @NamedQuery(name = "Userfinder.findByDeleteTime", query = "SELECT u FROM Userfinder u WHERE u.deleteTime = :deleteTime")})
public class Userfinder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "project_id")
    private int projectId;
    @Basic(optional = false)
    @Column(name = "role_id")
    private int roleId;
    @Basic(optional = false)
    @Column(name = "assigned_user_id")
    private int assignedUserId;
    @Basic(optional = false)
    @Lob
    @Column(name = "case_subject")
    private String caseSubject;
    @Basic(optional = false)
    @Lob
    @Column(name = "case_body")
    private String caseBody;
    @Basic(optional = false)
    @Column(name = "case_priority")
    private short casePriority;
    @Basic(optional = false)
    @Column(name = "attach_user")
    private boolean attachUser;
    @Basic(optional = false)
    @Column(name = "attach_content")
    private boolean attachContent;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Basic(optional = false)
    @Column(name = "last_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Basic(optional = false)
    @Column(name = "delete_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;

    public Userfinder() {
    }

    public Userfinder(Integer id) {
        this.id = id;
    }

    public Userfinder(Integer id, int projectId, int roleId, int assignedUserId, String caseSubject, String caseBody, short casePriority, boolean attachUser, boolean attachContent, short status, Date createTime, Date lastUpdateTime, Date deleteTime) {
        this.id = id;
        this.projectId = projectId;
        this.roleId = roleId;
        this.assignedUserId = assignedUserId;
        this.caseSubject = caseSubject;
        this.caseBody = caseBody;
        this.casePriority = casePriority;
        this.attachUser = attachUser;
        this.attachContent = attachContent;
        this.status = status;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
        this.deleteTime = deleteTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getCaseSubject() {
        return caseSubject;
    }

    public void setCaseSubject(String caseSubject) {
        this.caseSubject = caseSubject;
    }

    public String getCaseBody() {
        return caseBody;
    }

    public void setCaseBody(String caseBody) {
        this.caseBody = caseBody;
    }

    public short getCasePriority() {
        return casePriority;
    }

    public void setCasePriority(short casePriority) {
        this.casePriority = casePriority;
    }

    public boolean getAttachUser() {
        return attachUser;
    }

    public void setAttachUser(boolean attachUser) {
        this.attachUser = attachUser;
    }

    public boolean getAttachContent() {
        return attachContent;
    }

    public void setAttachContent(boolean attachContent) {
        this.attachContent = attachContent;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
        if (!(object instanceof Userfinder)) {
            return false;
        }
        Userfinder other = (Userfinder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Userfinder[ id=" + id + " ]";
    }
    
}
