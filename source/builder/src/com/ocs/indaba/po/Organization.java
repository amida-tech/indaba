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
@Table(name = "organization")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Organization.findAll", query = "SELECT o FROM Organization o"),
    @NamedQuery(name = "Organization.findById", query = "SELECT o FROM Organization o WHERE o.id = :id"),
    @NamedQuery(name = "Organization.findByName", query = "SELECT o FROM Organization o WHERE o.name = :name"),
    @NamedQuery(name = "Organization.findByAddress", query = "SELECT o FROM Organization o WHERE o.address = :address"),
    @NamedQuery(name = "Organization.findByAdminUserId", query = "SELECT o FROM Organization o WHERE o.adminUserId = :adminUserId"),
    @NamedQuery(name = "Organization.findByUrl", query = "SELECT o FROM Organization o WHERE o.url = :url"),
    @NamedQuery(name = "Organization.findByEnforceApiSecurity", query = "SELECT o FROM Organization o WHERE o.enforceApiSecurity = :enforceApiSecurity")})
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "admin_user_id")
    private int adminUserId;
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @Column(name = "enforce_api_security")
    private boolean enforceApiSecurity;

    public Organization() {
    }

    public Organization(Integer id) {
        this.id = id;
    }

    public Organization(Integer id, String name, int adminUserId, boolean enforceApiSecurity) {
        this.id = id;
        this.name = name;
        this.adminUserId = adminUserId;
        this.enforceApiSecurity = enforceApiSecurity;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(int adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getEnforceApiSecurity() {
        return enforceApiSecurity;
    }

    public void setEnforceApiSecurity(boolean enforceApiSecurity) {
        this.enforceApiSecurity = enforceApiSecurity;
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
        if (!(object instanceof Organization)) {
            return false;
        }
        Organization other = (Organization) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Organization[ id=" + id + " ]";
    }
    
}
