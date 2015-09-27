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
@Table(name = "journal_content_version")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JournalContentVersion.findAll", query = "SELECT j FROM JournalContentVersion j"),
    @NamedQuery(name = "JournalContentVersion.findById", query = "SELECT j FROM JournalContentVersion j WHERE j.id = :id"),
    @NamedQuery(name = "JournalContentVersion.findByContentVersionId", query = "SELECT j FROM JournalContentVersion j WHERE j.contentVersionId = :contentVersionId")})
public class JournalContentVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "content_version_id")
    private int contentVersionId;
    @Lob
    @Column(name = "body")
    private String body;

    public JournalContentVersion() {
    }

    public JournalContentVersion(Integer id) {
        this.id = id;
    }

    public JournalContentVersion(Integer id, int contentVersionId) {
        this.id = id;
        this.contentVersionId = contentVersionId;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
        if (!(object instanceof JournalContentVersion)) {
            return false;
        }
        JournalContentVersion other = (JournalContentVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.JournalContentVersion[ id=" + id + " ]";
    }
    
}
