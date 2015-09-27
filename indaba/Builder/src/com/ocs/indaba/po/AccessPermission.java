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
@Table(name = "access_permission")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccessPermission.findAll", query = "SELECT a FROM AccessPermission a"),
    @NamedQuery(name = "AccessPermission.findById", query = "SELECT a FROM AccessPermission a WHERE a.id = :id"),
    @NamedQuery(name = "AccessPermission.findByAccessMatrixId", query = "SELECT a FROM AccessPermission a WHERE a.accessMatrixId = :accessMatrixId"),
    @NamedQuery(name = "AccessPermission.findByRoleId", query = "SELECT a FROM AccessPermission a WHERE a.roleId = :roleId"),
    @NamedQuery(name = "AccessPermission.findByRightsId", query = "SELECT a FROM AccessPermission a WHERE a.rightsId = :rightsId"),
    @NamedQuery(name = "AccessPermission.findByPermission", query = "SELECT a FROM AccessPermission a WHERE a.permission = :permission")})
public class AccessPermission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "access_matrix_id")
    private int accessMatrixId;
    @Basic(optional = false)
    @Column(name = "role_id")
    private int roleId;
    @Basic(optional = false)
    @Column(name = "rights_id")
    private int rightsId;
    @Basic(optional = false)
    @Column(name = "permission")
    private short permission;

    public AccessPermission() {
    }

    public AccessPermission(Integer id) {
        this.id = id;
    }

    public AccessPermission(Integer id, int accessMatrixId, int roleId, int rightsId, short permission) {
        this.id = id;
        this.accessMatrixId = accessMatrixId;
        this.roleId = roleId;
        this.rightsId = rightsId;
        this.permission = permission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAccessMatrixId() {
        return accessMatrixId;
    }

    public void setAccessMatrixId(int accessMatrixId) {
        this.accessMatrixId = accessMatrixId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getRightsId() {
        return rightsId;
    }

    public void setRightsId(int rightsId) {
        this.rightsId = rightsId;
    }

    public short getPermission() {
        return permission;
    }

    public void setPermission(short permission) {
        this.permission = permission;
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
        if (!(object instanceof AccessPermission)) {
            return false;
        }
        AccessPermission other = (AccessPermission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AccessPermission[ id=" + id + " ]";
    }
    
}
