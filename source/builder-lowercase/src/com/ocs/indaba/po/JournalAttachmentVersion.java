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
@Table(name = "journal_attachment_version")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JournalAttachmentVersion.findAll", query = "SELECT j FROM JournalAttachmentVersion j"),
    @NamedQuery(name = "JournalAttachmentVersion.findById", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.id = :id"),
    @NamedQuery(name = "JournalAttachmentVersion.findByContentVersionId", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.contentVersionId = :contentVersionId"),
    @NamedQuery(name = "JournalAttachmentVersion.findByName", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.name = :name"),
    @NamedQuery(name = "JournalAttachmentVersion.findBySize", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.size = :size"),
    @NamedQuery(name = "JournalAttachmentVersion.findByType", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.type = :type"),
    @NamedQuery(name = "JournalAttachmentVersion.findByNote", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.note = :note"),
    @NamedQuery(name = "JournalAttachmentVersion.findByFilePath", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.filePath = :filePath"),
    @NamedQuery(name = "JournalAttachmentVersion.findByUpdateTime", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.updateTime = :updateTime"),
    @NamedQuery(name = "JournalAttachmentVersion.findByUserId", query = "SELECT j FROM JournalAttachmentVersion j WHERE j.userId = :userId")})
public class JournalAttachmentVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "content_version_id")
    private int contentVersionId;
    @Column(name = "name")
    private String name;
    @Column(name = "size")
    private Integer size;
    @Column(name = "type")
    private String type;
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "user_id")
    private Integer userId;

    public JournalAttachmentVersion() {
    }

    public JournalAttachmentVersion(Integer id) {
        this.id = id;
    }

    public JournalAttachmentVersion(Integer id, int contentVersionId, String filePath) {
        this.id = id;
        this.contentVersionId = contentVersionId;
        this.filePath = filePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getContentVersionId() {
        return contentVersionId;
    }

    public void setContentVersionId(int contentVersionId) {
        this.contentVersionId = contentVersionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        if (!(object instanceof JournalAttachmentVersion)) {
            return false;
        }
        JournalAttachmentVersion other = (JournalAttachmentVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.JournalAttachmentVersion[ id=" + id + " ]";
    }
    
}
