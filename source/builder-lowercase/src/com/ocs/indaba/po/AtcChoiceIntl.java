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
@Table(name = "atc_choice_intl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtcChoiceIntl.findAll", query = "SELECT a FROM AtcChoiceIntl a"),
    @NamedQuery(name = "AtcChoiceIntl.findById", query = "SELECT a FROM AtcChoiceIntl a WHERE a.id = :id"),
    @NamedQuery(name = "AtcChoiceIntl.findByAtcChoiceId", query = "SELECT a FROM AtcChoiceIntl a WHERE a.atcChoiceId = :atcChoiceId"),
    @NamedQuery(name = "AtcChoiceIntl.findByLanguageId", query = "SELECT a FROM AtcChoiceIntl a WHERE a.languageId = :languageId")})
public class AtcChoiceIntl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "atc_choice_id")
    private int atcChoiceId;
    @Basic(optional = false)
    @Column(name = "language_id")
    private int languageId;
    @Basic(optional = false)
    @Lob
    @Column(name = "label")
    private String label;
    @Lob
    @Column(name = "criteria")
    private String criteria;

    public AtcChoiceIntl() {
    }

    public AtcChoiceIntl(Integer id) {
        this.id = id;
    }

    public AtcChoiceIntl(Integer id, int atcChoiceId, int languageId, String label) {
        this.id = id;
        this.atcChoiceId = atcChoiceId;
        this.languageId = languageId;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAtcChoiceId() {
        return atcChoiceId;
    }

    public void setAtcChoiceId(int atcChoiceId) {
        this.atcChoiceId = atcChoiceId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
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
        if (!(object instanceof AtcChoiceIntl)) {
            return false;
        }
        AtcChoiceIntl other = (AtcChoiceIntl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AtcChoiceIntl[ id=" + id + " ]";
    }
    
}
