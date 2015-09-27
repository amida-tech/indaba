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
@Table(name = "itags")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Itags.findAll", query = "SELECT i FROM Itags i"),
    @NamedQuery(name = "Itags.findById", query = "SELECT i FROM Itags i WHERE i.id = :id"),
    @NamedQuery(name = "Itags.findByTerm", query = "SELECT i FROM Itags i WHERE i.term = :term"),
    @NamedQuery(name = "Itags.findByDescription", query = "SELECT i FROM Itags i WHERE i.description = :description")})
public class Itags implements Serializable {
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

    public Itags() {
    }

    public Itags(Integer id) {
        this.id = id;
    }

    public Itags(Integer id, String term) {
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
        if (!(object instanceof Itags)) {
            return false;
        }
        Itags other = (Itags) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Itags[ id=" + id + " ]";
    }
    
}
