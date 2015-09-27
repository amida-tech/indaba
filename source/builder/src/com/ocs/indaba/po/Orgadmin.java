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
@Table(name = "orgadmin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orgadmin.findAll", query = "SELECT o FROM Orgadmin o"),
    @NamedQuery(name = "Orgadmin.findById", query = "SELECT o FROM Orgadmin o WHERE o.id = :id"),
    @NamedQuery(name = "Orgadmin.findByOrganizationId", query = "SELECT o FROM Orgadmin o WHERE o.organizationId = :organizationId"),
    @NamedQuery(name = "Orgadmin.findByUserId", query = "SELECT o FROM Orgadmin o WHERE o.userId = :userId")})
public class Orgadmin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "organization_id")
    private int organizationId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;

    public Orgadmin() {
    }

    public Orgadmin(Integer id) {
        this.id = id;
    }

    public Orgadmin(Integer id, int organizationId, int userId) {
        this.id = id;
        this.organizationId = organizationId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        if (!(object instanceof Orgadmin)) {
            return false;
        }
        Orgadmin other = (Orgadmin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Orgadmin[ id=" + id + " ]";
    }
    
}
