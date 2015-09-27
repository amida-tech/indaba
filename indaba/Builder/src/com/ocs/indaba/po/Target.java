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
@Table(name = "target")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Target.findAll", query = "SELECT t FROM Target t"),
    @NamedQuery(name = "Target.findById", query = "SELECT t FROM Target t WHERE t.id = :id"),
    @NamedQuery(name = "Target.findByName", query = "SELECT t FROM Target t WHERE t.name = :name"),
    @NamedQuery(name = "Target.findByDescription", query = "SELECT t FROM Target t WHERE t.description = :description"),
    @NamedQuery(name = "Target.findByTargetType", query = "SELECT t FROM Target t WHERE t.targetType = :targetType"),
    @NamedQuery(name = "Target.findByShortName", query = "SELECT t FROM Target t WHERE t.shortName = :shortName"),
    @NamedQuery(name = "Target.findByGuid", query = "SELECT t FROM Target t WHERE t.guid = :guid"),
    @NamedQuery(name = "Target.findByCreatorOrgId", query = "SELECT t FROM Target t WHERE t.creatorOrgId = :creatorOrgId"),
    @NamedQuery(name = "Target.findByOwnerOrgId", query = "SELECT t FROM Target t WHERE t.ownerOrgId = :ownerOrgId"),
    @NamedQuery(name = "Target.findByVisibility", query = "SELECT t FROM Target t WHERE t.visibility = :visibility"),
    @NamedQuery(name = "Target.findByLanguageId", query = "SELECT t FROM Target t WHERE t.languageId = :languageId"),
    @NamedQuery(name = "Target.findByStatus", query = "SELECT t FROM Target t WHERE t.status = :status"),
    @NamedQuery(name = "Target.findByCreateTime", query = "SELECT t FROM Target t WHERE t.createTime = :createTime"),
    @NamedQuery(name = "Target.findByDeleteTime", query = "SELECT t FROM Target t WHERE t.deleteTime = :deleteTime")})
public class Target implements Serializable {
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
    @Column(name = "target_type")
    private short targetType;
    @Basic(optional = false)
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "guid")
    private String guid;
    @Basic(optional = false)
    @Column(name = "creator_org_id")
    private int creatorOrgId;
    @Basic(optional = false)
    @Column(name = "owner_org_id")
    private int ownerOrgId;
    @Basic(optional = false)
    @Column(name = "visibility")
    private short visibility;
    @Basic(optional = false)
    @Column(name = "language_id")
    private int languageId;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "delete_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;

    public Target() {
    }

    public Target(Integer id) {
        this.id = id;
    }

    public Target(Integer id, String name, short targetType, String shortName, int creatorOrgId, int ownerOrgId, short visibility, int languageId, short status) {
        this.id = id;
        this.name = name;
        this.targetType = targetType;
        this.shortName = shortName;
        this.creatorOrgId = creatorOrgId;
        this.ownerOrgId = ownerOrgId;
        this.visibility = visibility;
        this.languageId = languageId;
        this.status = status;
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

    public short getTargetType() {
        return targetType;
    }

    public void setTargetType(short targetType) {
        this.targetType = targetType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getCreatorOrgId() {
        return creatorOrgId;
    }

    public void setCreatorOrgId(int creatorOrgId) {
        this.creatorOrgId = creatorOrgId;
    }

    public int getOwnerOrgId() {
        return ownerOrgId;
    }

    public void setOwnerOrgId(int ownerOrgId) {
        this.ownerOrgId = ownerOrgId;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
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

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
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
        if (!(object instanceof Target)) {
            return false;
        }
        Target other = (Target) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Target[ id=" + id + " ]";
    }
    
}
