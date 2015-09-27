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
@Table(name = "access_matrix")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccessMatrix.findAll", query = "SELECT a FROM AccessMatrix a"),
    @NamedQuery(name = "AccessMatrix.findById", query = "SELECT a FROM AccessMatrix a WHERE a.id = :id"),
    @NamedQuery(name = "AccessMatrix.findByName", query = "SELECT a FROM AccessMatrix a WHERE a.name = :name"),
    @NamedQuery(name = "AccessMatrix.findByDescription", query = "SELECT a FROM AccessMatrix a WHERE a.description = :description"),
    @NamedQuery(name = "AccessMatrix.findByDefaultValue", query = "SELECT a FROM AccessMatrix a WHERE a.defaultValue = :defaultValue")})
public class AccessMatrix implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "default_value")
    private Short defaultValue;

    public AccessMatrix() {
    }

    public AccessMatrix(Integer id) {
        this.id = id;
    }

    public AccessMatrix(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Short getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Short defaultValue) {
        this.defaultValue = defaultValue;
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
        if (!(object instanceof AccessMatrix)) {
            return false;
        }
        AccessMatrix other = (AccessMatrix) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AccessMatrix[ id=" + id + " ]";
    }
    
}
