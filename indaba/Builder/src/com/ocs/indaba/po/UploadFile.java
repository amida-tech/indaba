/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "upload_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UploadFile.findAll", query = "SELECT u FROM UploadFile u"),
    @NamedQuery(name = "UploadFile.findById", query = "SELECT u FROM UploadFile u WHERE u.id = :id"),
    @NamedQuery(name = "UploadFile.findByFileName", query = "SELECT u FROM UploadFile u WHERE u.fileName = :fileName"),
    @NamedQuery(name = "UploadFile.findByFilePath", query = "SELECT u FROM UploadFile u WHERE u.filePath = :filePath"),
    @NamedQuery(name = "UploadFile.findByDisplayName", query = "SELECT u FROM UploadFile u WHERE u.displayName = :displayName"),
    @NamedQuery(name = "UploadFile.findBySize", query = "SELECT u FROM UploadFile u WHERE u.size = :size"),
    @NamedQuery(name = "UploadFile.findByType", query = "SELECT u FROM UploadFile u WHERE u.type = :type"),
    @NamedQuery(name = "UploadFile.findByNote", query = "SELECT u FROM UploadFile u WHERE u.note = :note"),
    @NamedQuery(name = "UploadFile.findByUpdateTime", query = "SELECT u FROM UploadFile u WHERE u.updateTime = :updateTime"),
    @NamedQuery(name = "UploadFile.findByUserId", query = "SELECT u FROM UploadFile u WHERE u.userId = :userId")})
public class UploadFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "file_name")
    private String fileName;
    @Basic(optional = false)
    @Column(name = "file_path")
    private String filePath;
    @Basic(optional = false)
    @Column(name = "display_name")
    private String displayName;
    @Basic(optional = false)
    @Column(name = "size")
    private int size;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "user_id")
    private Integer userId;

    public UploadFile() {
    }

    public UploadFile(Integer id) {
        this.id = id;
    }

    public UploadFile(Integer id, String fileName, String filePath, String displayName, int size, String type, Date updateTime) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.displayName = displayName;
        this.size = size;
        this.type = type;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
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
        if (!(object instanceof UploadFile)) {
            return false;
        }
        UploadFile other = (UploadFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.UploadFile[ id=" + id + " ]";
    }
    
}
