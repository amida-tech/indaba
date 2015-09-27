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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "imports")
@NamedQueries({
    @NamedQuery(name = "Imports.findAll", query = "SELECT i FROM Imports i"),
    @NamedQuery(name = "Imports.findById", query = "SELECT i FROM Imports i WHERE i.id = :id"),
    @NamedQuery(name = "Imports.findByFolder", query = "SELECT i FROM Imports i WHERE i.folder = :folder"),
    @NamedQuery(name = "Imports.findByStartTime", query = "SELECT i FROM Imports i WHERE i.startTime = :startTime"),
    @NamedQuery(name = "Imports.findByEndTime", query = "SELECT i FROM Imports i WHERE i.endTime = :endTime"),
    @NamedQuery(name = "Imports.findByResult", query = "SELECT i FROM Imports i WHERE i.result = :result")})
public class Imports implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "folder")
    private String folder;
    @Lob
    @Column(name = "note")
    private String note;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "result")
    private Short result;

    public Imports() {
    }

    public Imports(Integer id) {
        this.id = id;
    }

    public Imports(Integer id, String folder) {
        this.id = id;
        this.folder = folder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getResult() {
        return result;
    }

    public void setResult(Short result) {
        this.result = result;
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
        if (!(object instanceof Imports)) {
            return false;
        }
        Imports other = (Imports) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Imports[id=" + id + "]";
    }

}
