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
@Table(name = "view_matrix")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewMatrix.findAll", query = "SELECT v FROM ViewMatrix v"),
    @NamedQuery(name = "ViewMatrix.findById", query = "SELECT v FROM ViewMatrix v WHERE v.id = :id"),
    @NamedQuery(name = "ViewMatrix.findByName", query = "SELECT v FROM ViewMatrix v WHERE v.name = :name"),
    @NamedQuery(name = "ViewMatrix.findByDescription", query = "SELECT v FROM ViewMatrix v WHERE v.description = :description"),
    @NamedQuery(name = "ViewMatrix.findByDefaultValue", query = "SELECT v FROM ViewMatrix v WHERE v.defaultValue = :defaultValue")})
public class ViewMatrix implements Serializable {
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
    @Basic(optional = false)
    @Column(name = "default_value")
    private short defaultValue;

    public ViewMatrix() {
    }

    public ViewMatrix(Integer id) {
        this.id = id;
    }

    public ViewMatrix(Integer id, String name, short defaultValue) {
        this.id = id;
        this.name = name;
        this.defaultValue = defaultValue;
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

    public short getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(short defaultValue) {
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
        if (!(object instanceof ViewMatrix)) {
            return false;
        }
        ViewMatrix other = (ViewMatrix) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ViewMatrix[ id=" + id + " ]";
    }
    
}
