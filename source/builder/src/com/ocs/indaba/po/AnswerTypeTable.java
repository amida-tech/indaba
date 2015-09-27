/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

import java.io.Serializable;
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

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "answer_type_table")
@NamedQueries({
    @NamedQuery(name = "AnswerTypeTable.findAll", query = "SELECT a FROM AnswerTypeTable a"),
    @NamedQuery(name = "AnswerTypeTable.findById", query = "SELECT a FROM AnswerTypeTable a WHERE a.id = :id"),
    @NamedQuery(name = "AnswerTypeTable.findByTdfFileName", query = "SELECT a FROM AnswerTypeTable a WHERE a.tdfFileName = :tdfFileName"),
    @NamedQuery(name = "AnswerTypeTable.findByFilePath", query = "SELECT a FROM AnswerTypeTable a WHERE a.filePath = :filePath")})
public class AnswerTypeTable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tdf_file_name")
    private String tdfFileName;
    @Column(name = "file_path")
    private String filePath;
    @Lob
    @Column(name = "data")
    private String data;

    public AnswerTypeTable() {
    }

    public AnswerTypeTable(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTdfFileName() {
        return tdfFileName;
    }

    public void setTdfFileName(String tdfFileName) {
        this.tdfFileName = tdfFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        if (!(object instanceof AnswerTypeTable)) {
            return false;
        }
        AnswerTypeTable other = (AnswerTypeTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerTypeTable[id=" + id + "]";
    }

}
