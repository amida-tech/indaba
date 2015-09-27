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
@Table(name = "tokenset")
@NamedQueries({
    @NamedQuery(name = "Tokenset.findAll", query = "SELECT t FROM Tokenset t"),
    @NamedQuery(name = "Tokenset.findById", query = "SELECT t FROM Tokenset t WHERE t.id = :id"),
    @NamedQuery(name = "Tokenset.findByCatgeory", query = "SELECT t FROM Tokenset t WHERE t.catgeory = :catgeory")})
public class Tokenset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "catgeory")
    private int catgeory;
    @Lob
    @Column(name = "tokens")
    private String tokens;

    public Tokenset() {
    }

    public Tokenset(Integer id) {
        this.id = id;
    }

    public Tokenset(Integer id, int catgeory) {
        this.id = id;
        this.catgeory = catgeory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCatgeory() {
        return catgeory;
    }

    public void setCatgeory(int catgeory) {
        this.catgeory = catgeory;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
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
        if (!(object instanceof Tokenset)) {
            return false;
        }
        Tokenset other = (Tokenset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Tokenset[id=" + id + "]";
    }

}
