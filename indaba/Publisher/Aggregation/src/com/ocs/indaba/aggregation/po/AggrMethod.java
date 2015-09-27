/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.po;

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
 * @author Administrator
 */
@Entity
@Table(name = "aggr_method")
@NamedQueries({
    @NamedQuery(name = "AggrMethod.findAll", query = "SELECT a FROM AggrMethod a"),
    @NamedQuery(name = "AggrMethod.findById", query = "SELECT a FROM AggrMethod a WHERE a.id = :id"),
    @NamedQuery(name = "AggrMethod.findByName", query = "SELECT a FROM AggrMethod a WHERE a.name = :name"),
    @NamedQuery(name = "AggrMethod.findByDescription", query = "SELECT a FROM AggrMethod a WHERE a.description = :description"),
    @NamedQuery(name = "AggrMethod.findByModuleName", query = "SELECT a FROM AggrMethod a WHERE a.moduleName = :moduleName")})
public class AggrMethod implements Serializable {
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
    @Column(name = "module_name")
    private String moduleName;

    public AggrMethod() {
    }

    public AggrMethod(Integer id) {
        this.id = id;
    }

    public AggrMethod(Integer id, String name) {
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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
        if (!(object instanceof AggrMethod)) {
            return false;
        }
        AggrMethod other = (AggrMethod) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.AggrMethod[id=" + id + "]";
    }

}
