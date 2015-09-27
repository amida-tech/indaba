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
@Table(name = "noteobj_version_intl")
@NamedQueries({
    @NamedQuery(name = "NoteobjVersionIntl.findAll", query = "SELECT n FROM NoteobjVersionIntl n"),
    @NamedQuery(name = "NoteobjVersionIntl.findById", query = "SELECT n FROM NoteobjVersionIntl n WHERE n.id = :id"),
    @NamedQuery(name = "NoteobjVersionIntl.findByNoteobjVersionId", query = "SELECT n FROM NoteobjVersionIntl n WHERE n.noteobjVersionId = :noteobjVersionId"),
    @NamedQuery(name = "NoteobjVersionIntl.findByLanguageId", query = "SELECT n FROM NoteobjVersionIntl n WHERE n.languageId = :languageId")})
public class NoteobjVersionIntl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "noteobj_version_id")
    private int noteobjVersionId;
    @Basic(optional = false)
    @Column(name = "language_id")
    private int languageId;
    @Lob
    @Column(name = "note")
    private String note;

    public NoteobjVersionIntl() {
    }

    public NoteobjVersionIntl(Integer id) {
        this.id = id;
    }

    public NoteobjVersionIntl(Integer id, int noteobjVersionId, int languageId) {
        this.id = id;
        this.noteobjVersionId = noteobjVersionId;
        this.languageId = languageId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNoteobjVersionId() {
        return noteobjVersionId;
    }

    public void setNoteobjVersionId(int noteobjVersionId) {
        this.noteobjVersionId = noteobjVersionId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
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
        if (!(object instanceof NoteobjVersionIntl)) {
            return false;
        }
        NoteobjVersionIntl other = (NoteobjVersionIntl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.NoteobjVersionIntl[id=" + id + "]";
    }

}
