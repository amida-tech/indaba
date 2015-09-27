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
@Table(name = "journal_content_object")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JournalContentObject.findAll", query = "SELECT j FROM JournalContentObject j"),
    @NamedQuery(name = "JournalContentObject.findById", query = "SELECT j FROM JournalContentObject j WHERE j.id = :id"),
    @NamedQuery(name = "JournalContentObject.findByContentHeaderId", query = "SELECT j FROM JournalContentObject j WHERE j.contentHeaderId = :contentHeaderId"),
    @NamedQuery(name = "JournalContentObject.findByJournalConfigId", query = "SELECT j FROM JournalContentObject j WHERE j.journalConfigId = :journalConfigId")})
public class JournalContentObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "content_header_id")
    private int contentHeaderId;
    @Lob
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @Column(name = "journal_config_id")
    private int journalConfigId;

    public JournalContentObject() {
    }

    public JournalContentObject(Integer id) {
        this.id = id;
    }

    public JournalContentObject(Integer id, int contentHeaderId, int journalConfigId) {
        this.id = id;
        this.contentHeaderId = contentHeaderId;
        this.journalConfigId = journalConfigId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getContentHeaderId() {
        return contentHeaderId;
    }

    public void setContentHeaderId(int contentHeaderId) {
        this.contentHeaderId = contentHeaderId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getJournalConfigId() {
        return journalConfigId;
    }

    public void setJournalConfigId(int journalConfigId) {
        this.journalConfigId = journalConfigId;
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
        if (!(object instanceof JournalContentObject)) {
            return false;
        }
        JournalContentObject other = (JournalContentObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.JournalContentObject[ id=" + id + " ]";
    }
    
}
