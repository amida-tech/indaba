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
@Table(name = "groupdef")
@NamedQueries({
    @NamedQuery(name = "Groupdef.findAll", query = "SELECT g FROM Groupdef g"),
    @NamedQuery(name = "Groupdef.findById", query = "SELECT g FROM Groupdef g WHERE g.id = :id"),
    @NamedQuery(name = "Groupdef.findByProductId", query = "SELECT g FROM Groupdef g WHERE g.productId = :productId"),
    @NamedQuery(name = "Groupdef.findByName", query = "SELECT g FROM Groupdef g WHERE g.name = :name"),
    @NamedQuery(name = "Groupdef.findByWeight", query = "SELECT g FROM Groupdef g WHERE g.weight = :weight"),
    @NamedQuery(name = "Groupdef.findByEnabled", query = "SELECT g FROM Groupdef g WHERE g.enabled = :enabled")})
public class Groupdef implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "weight")
    private int weight;
    @Column(name = "enabled")
    private Boolean enabled;

    public Groupdef() {
    }

    public Groupdef(Integer id) {
        this.id = id;
    }

    public Groupdef(Integer id, int productId, String name, int weight) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
        if (!(object instanceof Groupdef)) {
            return false;
        }
        Groupdef other = (Groupdef) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Groupdef[id=" + id + "]";
    }

}
