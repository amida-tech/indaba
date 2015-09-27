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
@Table(name = "rights")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rights.findAll", query = "SELECT r FROM Rights r"),
    @NamedQuery(name = "Rights.findById", query = "SELECT r FROM Rights r WHERE r.id = :id"),
    @NamedQuery(name = "Rights.findByRightCategoryId", query = "SELECT r FROM Rights r WHERE r.rightCategoryId = :rightCategoryId"),
    @NamedQuery(name = "Rights.findByName", query = "SELECT r FROM Rights r WHERE r.name = :name"),
    @NamedQuery(name = "Rights.findByLabel", query = "SELECT r FROM Rights r WHERE r.label = :label"),
    @NamedQuery(name = "Rights.findByDescription", query = "SELECT r FROM Rights r WHERE r.description = :description")})
public class Rights implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "right_category_id")
    private int rightCategoryId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @Column(name = "description")
    private String description;

    public Rights() {
    }

    public Rights(Integer id) {
        this.id = id;
    }

    public Rights(Integer id, int rightCategoryId, String name, String label) {
        this.id = id;
        this.rightCategoryId = rightCategoryId;
        this.name = name;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRightCategoryId() {
        return rightCategoryId;
    }

    public void setRightCategoryId(int rightCategoryId) {
        this.rightCategoryId = rightCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        if (!(object instanceof Rights)) {
            return false;
        }
        Rights other = (Rights) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Rights[ id=" + id + " ]";
    }
    
}
