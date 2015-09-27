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
@Table(name = "source_file_text_resource")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SourceFileTextResource.findAll", query = "SELECT s FROM SourceFileTextResource s"),
    @NamedQuery(name = "SourceFileTextResource.findById", query = "SELECT s FROM SourceFileTextResource s WHERE s.id = :id"),
    @NamedQuery(name = "SourceFileTextResource.findBySourceFileId", query = "SELECT s FROM SourceFileTextResource s WHERE s.sourceFileId = :sourceFileId"),
    @NamedQuery(name = "SourceFileTextResource.findByTextResourceId", query = "SELECT s FROM SourceFileTextResource s WHERE s.textResourceId = :textResourceId")})
public class SourceFileTextResource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "source_file_id")
    private int sourceFileId;
    @Basic(optional = false)
    @Column(name = "text_resource_id")
    private int textResourceId;

    public SourceFileTextResource() {
    }

    public SourceFileTextResource(Integer id) {
        this.id = id;
    }

    public SourceFileTextResource(Integer id, int sourceFileId, int textResourceId) {
        this.id = id;
        this.sourceFileId = sourceFileId;
        this.textResourceId = textResourceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSourceFileId() {
        return sourceFileId;
    }

    public void setSourceFileId(int sourceFileId) {
        this.sourceFileId = sourceFileId;
    }

    public int getTextResourceId() {
        return textResourceId;
    }

    public void setTextResourceId(int textResourceId) {
        this.textResourceId = textResourceId;
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
        if (!(object instanceof SourceFileTextResource)) {
            return false;
        }
        SourceFileTextResource other = (SourceFileTextResource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SourceFileTextResource{" + "id=" + id + "sourceFileId=" + sourceFileId + "textResourceId=" + textResourceId + '}';
    }
    
}
