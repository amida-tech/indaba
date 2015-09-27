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
@Table(name = "case_tag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CaseTag.findAll", query = "SELECT c FROM CaseTag c"),
    @NamedQuery(name = "CaseTag.findById", query = "SELECT c FROM CaseTag c WHERE c.id = :id"),
    @NamedQuery(name = "CaseTag.findByCasesId", query = "SELECT c FROM CaseTag c WHERE c.casesId = :casesId"),
    @NamedQuery(name = "CaseTag.findByCtagsId", query = "SELECT c FROM CaseTag c WHERE c.ctagsId = :ctagsId")})
public class CaseTag implements Serializable {
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
    @Column(name = "ctags_id")
    private int ctagsId;

    public CaseTag() {
    }

    public CaseTag(Integer id) {
        this.id = id;
    }

    public CaseTag(Integer id, int casesId, int ctagsId) {
        this.id = id;
        this.casesId = casesId;
        this.ctagsId = ctagsId;
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

    public int getCtagsId() {
        return ctagsId;
    }

    public void setCtagsId(int ctagsId) {
        this.ctagsId = ctagsId;
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
        if (!(object instanceof CaseTag)) {
            return false;
        }
        CaseTag other = (CaseTag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.CaseTag[ id=" + id + " ]";
    }
    
}
