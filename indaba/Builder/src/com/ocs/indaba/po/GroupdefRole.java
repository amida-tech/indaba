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
@Table(name = "groupdef_role")
@NamedQueries({
    @NamedQuery(name = "GroupdefRole.findAll", query = "SELECT g FROM GroupdefRole g"),
    @NamedQuery(name = "GroupdefRole.findById", query = "SELECT g FROM GroupdefRole g WHERE g.id = :id"),
    @NamedQuery(name = "GroupdefRole.findByGroupdefId", query = "SELECT g FROM GroupdefRole g WHERE g.groupdefId = :groupdefId"),
    @NamedQuery(name = "GroupdefRole.findByRoleId", query = "SELECT g FROM GroupdefRole g WHERE g.roleId = :roleId"),
    @NamedQuery(name = "GroupdefRole.findByPermissions", query = "SELECT g FROM GroupdefRole g WHERE g.permissions = :permissions")})
public class GroupdefRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "groupdef_id")
    private int groupdefId;
    @Basic(optional = false)
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "permissions")
    private Integer permissions;

    public GroupdefRole() {
    }

    public GroupdefRole(Integer id) {
        this.id = id;
    }

    public GroupdefRole(Integer id, int groupdefId, int roleId) {
        this.id = id;
        this.groupdefId = groupdefId;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGroupdefId() {
        return groupdefId;
    }

    public void setGroupdefId(int groupdefId) {
        this.groupdefId = groupdefId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        this.permissions = permissions;
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
        if (!(object instanceof GroupdefRole)) {
            return false;
        }
        GroupdefRole other = (GroupdefRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.GroupdefRole[id=" + id + "]";
    }

}
