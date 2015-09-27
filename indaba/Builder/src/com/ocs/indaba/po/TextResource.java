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
@Table(name = "text_resource")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TextResource.findAll", query = "SELECT t FROM TextResource t"),
    @NamedQuery(name = "TextResource.findById", query = "SELECT t FROM TextResource t WHERE t.id = :id"),
    @NamedQuery(name = "TextResource.findByResourceName", query = "SELECT t FROM TextResource t WHERE t.resourceName = :resourceName"),
    @NamedQuery(name = "TextResource.findByDescription", query = "SELECT t FROM TextResource t WHERE t.description = :description")})
public class TextResource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "resource_name")
    private String resourceName;
    @Column(name = "description")
    private String description;

    public TextResource() {
    }

    public TextResource(Integer id) {
        this.id = id;
    }

    public TextResource(Integer id, String resourceName) {
        this.id = id;
        this.resourceName = resourceName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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
        if (!(object instanceof TextResource)) {
            return false;
        }
        TextResource other = (TextResource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.TextResource[ id=" + id + " ]";
    }
    
}
