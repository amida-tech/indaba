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
@Table(name = "ctags")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ctags.findAll", query = "SELECT c FROM Ctags c"),
    @NamedQuery(name = "Ctags.findById", query = "SELECT c FROM Ctags c WHERE c.id = :id"),
    @NamedQuery(name = "Ctags.findByTerm", query = "SELECT c FROM Ctags c WHERE c.term = :term"),
    @NamedQuery(name = "Ctags.findByDescription", query = "SELECT c FROM Ctags c WHERE c.description = :description")})
public class Ctags implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "term")
    private String term;
    @Column(name = "description")
    private String description;

    public Ctags() {
    }

    public Ctags(Integer id) {
        this.id = id;
    }

    public Ctags(Integer id, String term) {
        this.id = id;
        this.term = term;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof Ctags)) {
            return false;
        }
        Ctags other = (Ctags) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Ctags[ id=" + id + " ]";
    }
    
}
