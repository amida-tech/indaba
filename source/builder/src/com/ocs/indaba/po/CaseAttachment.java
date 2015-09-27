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
@Table(name = "case_attachment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CaseAttachment.findAll", query = "SELECT c FROM CaseAttachment c"),
    @NamedQuery(name = "CaseAttachment.findById", query = "SELECT c FROM CaseAttachment c WHERE c.id = :id"),
    @NamedQuery(name = "CaseAttachment.findByCasesId", query = "SELECT c FROM CaseAttachment c WHERE c.casesId = :casesId"),
    @NamedQuery(name = "CaseAttachment.findByFileName", query = "SELECT c FROM CaseAttachment c WHERE c.fileName = :fileName"),
    @NamedQuery(name = "CaseAttachment.findByFilePath", query = "SELECT c FROM CaseAttachment c WHERE c.filePath = :filePath")})
public class CaseAttachment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cases_id")
    private int casesId;
    @Basic(optional = false)
    @Column(name = "file_name")
    private String fileName;
    @Basic(optional = false)
    @Column(name = "file_path")
    private String filePath;

    public CaseAttachment() {
    }

    public CaseAttachment(Integer id) {
        this.id = id;
    }

    public CaseAttachment(Integer id, int casesId, String fileName, String filePath) {
        this.id = id;
        this.casesId = casesId;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCasesId() {
        return casesId;
    }

    public void setCasesId(int casesId) {
        this.casesId = casesId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CaseAttachment)) {
            return false;
        }
        CaseAttachment other = (CaseAttachment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.CaseAttachment[ id=" + id + " ]";
    }
    
}
