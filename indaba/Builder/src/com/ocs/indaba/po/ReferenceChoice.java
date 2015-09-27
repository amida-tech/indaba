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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "reference_choice")
@NamedQueries({
    @NamedQuery(name = "ReferenceChoice.findAll", query = "SELECT r FROM ReferenceChoice r"),
    @NamedQuery(name = "ReferenceChoice.findById", query = "SELECT r FROM ReferenceChoice r WHERE r.id = :id"),
    @NamedQuery(name = "ReferenceChoice.findByReferenceId", query = "SELECT r FROM ReferenceChoice r WHERE r.referenceId = :referenceId"),
    @NamedQuery(name = "ReferenceChoice.findByLabel", query = "SELECT r FROM ReferenceChoice r WHERE r.label = :label"),
    @NamedQuery(name = "ReferenceChoice.findByWeight", query = "SELECT r FROM ReferenceChoice r WHERE r.weight = :weight"),
    @NamedQuery(name = "ReferenceChoice.findByMask", query = "SELECT r FROM ReferenceChoice r WHERE r.mask = :mask"),
    @NamedQuery(name = "ReferenceChoice.findBySname", query = "SELECT r FROM ReferenceChoice r WHERE r.sname = :sname")})
public class ReferenceChoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "reference_id")
    private int referenceId;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @Column(name = "weight")
    private Integer weight;
    @Basic(optional = false)
    @Column(name = "mask")
    private long mask;
    @Column(name = "sname")
    private String sname;

    public ReferenceChoice() {
    }

    public ReferenceChoice(Integer id) {
        this.id = id;
    }

    public ReferenceChoice(Integer id, int referenceId, String label, long mask) {
        this.id = id;
        this.referenceId = referenceId;
        this.label = label;
        this.mask = mask;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public long getMask() {
        return mask;
    }

    public void setMask(long mask) {
        this.mask = mask;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
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
        if (!(object instanceof ReferenceChoice)) {
            return false;
        }
        ReferenceChoice other = (ReferenceChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ReferenceChoice[id=" + id + "]";
    }

}
