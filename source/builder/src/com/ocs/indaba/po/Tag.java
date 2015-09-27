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
@Table(name = "tag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tag.findAll", query = "SELECT t FROM Tag t"),
    @NamedQuery(name = "Tag.findById", query = "SELECT t FROM Tag t WHERE t.id = :id"),
    @NamedQuery(name = "Tag.findByTagType", query = "SELECT t FROM Tag t WHERE t.tagType = :tagType"),
    @NamedQuery(name = "Tag.findByTaggedObjectType", query = "SELECT t FROM Tag t WHERE t.taggedObjectType = :taggedObjectType"),
    @NamedQuery(name = "Tag.findByTaggedObjectId", query = "SELECT t FROM Tag t WHERE t.taggedObjectId = :taggedObjectId"),
    @NamedQuery(name = "Tag.findByTaggedObjectScopeId", query = "SELECT t FROM Tag t WHERE t.taggedObjectScopeId = :taggedObjectScopeId"),
    @NamedQuery(name = "Tag.findByTaggingTime", query = "SELECT t FROM Tag t WHERE t.taggingTime = :taggingTime"),
    @NamedQuery(name = "Tag.findByUserId", query = "SELECT t FROM Tag t WHERE t.userId = :userId"),
    @NamedQuery(name = "Tag.findByLabel", query = "SELECT t FROM Tag t WHERE t.label = :label")})
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tag_type")
    private short tagType;
    @Basic(optional = false)
    @Column(name = "tagged_object_type")
    private short taggedObjectType;
    @Basic(optional = false)
    @Column(name = "tagged_object_id")
    private int taggedObjectId;
    @Column(name = "tagged_object_scope_id")
    private Integer taggedObjectScopeId;
    @Basic(optional = false)
    @Column(name = "tagging_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date taggingTime;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;

    public Tag() {
    }

    public Tag(Integer id) {
        this.id = id;
    }

    public Tag(Integer id, short tagType, short taggedObjectType, int taggedObjectId, Date taggingTime, int userId, String label) {
        this.id = id;
        this.tagType = tagType;
        this.taggedObjectType = taggedObjectType;
        this.taggedObjectId = taggedObjectId;
        this.taggingTime = taggingTime;
        this.userId = userId;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getTagType() {
        return tagType;
    }

    public void setTagType(short tagType) {
        this.tagType = tagType;
    }

    public short getTaggedObjectType() {
        return taggedObjectType;
    }

    public void setTaggedObjectType(short taggedObjectType) {
        this.taggedObjectType = taggedObjectType;
    }

    public int getTaggedObjectId() {
        return taggedObjectId;
    }

    public void setTaggedObjectId(int taggedObjectId) {
        this.taggedObjectId = taggedObjectId;
    }

    public Integer getTaggedObjectScopeId() {
        return taggedObjectScopeId;
    }

    public void setTaggedObjectScopeId(Integer taggedObjectScopeId) {
        this.taggedObjectScopeId = taggedObjectScopeId;
    }

    public Date getTaggingTime() {
        return taggingTime;
    }

    public void setTaggingTime(Date taggingTime) {
        this.taggingTime = taggingTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        if (!(object instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Tag[ id=" + id + " ]";
    }
    
}
