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
@Table(name = "groupdef_user")
@NamedQueries({
    @NamedQuery(name = "GroupdefUser.findAll", query = "SELECT g FROM GroupdefUser g"),
    @NamedQuery(name = "GroupdefUser.findById", query = "SELECT g FROM GroupdefUser g WHERE g.id = :id"),
    @NamedQuery(name = "GroupdefUser.findByGroupdefId", query = "SELECT g FROM GroupdefUser g WHERE g.groupdefId = :groupdefId"),
    @NamedQuery(name = "GroupdefUser.findByUserId", query = "SELECT g FROM GroupdefUser g WHERE g.userId = :userId"),
    @NamedQuery(name = "GroupdefUser.findByPermissions", query = "SELECT g FROM GroupdefUser g WHERE g.permissions = :permissions")})
public class GroupdefUser implements Serializable {
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
    @Column(name = "user_id")
    private int userId;
    @Column(name = "permissions")
    private Integer permissions;

    public GroupdefUser() {
    }

    public GroupdefUser(Integer id) {
        this.id = id;
    }

    public GroupdefUser(Integer id, int groupdefId, int userId) {
        this.id = id;
        this.groupdefId = groupdefId;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        if (!(object instanceof GroupdefUser)) {
            return false;
        }
        GroupdefUser other = (GroupdefUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.GroupdefUser[id=" + id + "]";
    }

}
