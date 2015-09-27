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
@Table(name = "source_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SourceFile.findAll", query = "SELECT s FROM SourceFile s"),
    @NamedQuery(name = "SourceFile.findById", query = "SELECT s FROM SourceFile s WHERE s.id = :id"),
    @NamedQuery(name = "SourceFile.findByFilename", query = "SELECT s FROM SourceFile s WHERE s.filename = :filename"),
    @NamedQuery(name = "SourceFile.findByPath", query = "SELECT s FROM SourceFile s WHERE s.path = :path"),
    @NamedQuery(name = "SourceFile.findByExtension", query = "SELECT s FROM SourceFile s WHERE s.extension = :extension"),
    @NamedQuery(name = "SourceFile.findByStatus", query = "SELECT s FROM SourceFile s WHERE s.status = :status"),
    @NamedQuery(name = "SourceFile.findByLastUpdateTime", query = "SELECT s FROM SourceFile s WHERE s.lastUpdateTime = :lastUpdateTime"),
    @NamedQuery(name = "SourceFile.findByNote", query = "SELECT s FROM SourceFile s WHERE s.note = :note")})
public class SourceFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "filename")
    private String filename;
    @Basic(optional = false)
    @Column(name = "path")
    private String path;
    @Basic(optional = false)
    @Column(name = "extension")
    private String extension;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "last_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Column(name = "note")
    private String note;

    public SourceFile() {
    }

    public SourceFile(Integer id) {
        this.id = id;
    }

    public SourceFile(Integer id, String filename, String path, String extension, short status) {
        this.id = id;
        this.filename = filename;
        this.path = path;
        this.extension = extension;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        if (!(object instanceof SourceFile)) {
            return false;
        }
        SourceFile other = (SourceFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SourceFile[ id=" + id + " ]";
    }
    
}
