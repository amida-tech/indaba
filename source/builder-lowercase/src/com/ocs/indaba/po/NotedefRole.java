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
@Table(name = "notedef_role")
@NamedQueries({
    @NamedQuery(name = "NotedefRole.findAll", query = "SELECT n FROM NotedefRole n"),
    @NamedQuery(name = "NotedefRole.findById", query = "SELECT n FROM NotedefRole n WHERE n.id = :id"),
    @NamedQuery(name = "NotedefRole.findByNotedefId", query = "SELECT n FROM NotedefRole n WHERE n.notedefId = :notedefId"),
    @NamedQuery(name = "NotedefRole.findByRoleId", query = "SELECT n FROM NotedefRole n WHERE n.roleId = :roleId"),
    @NamedQuery(name = "NotedefRole.findByPermissions", query = "SELECT n FROM NotedefRole n WHERE n.permissions = :permissions")})
public class NotedefRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "notedef_id")
    private int notedefId;
    @Basic(optional = false)
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "permissions")
    private Integer permissions;

    public NotedefRole() {
    }

    public NotedefRole(Integer id) {
        this.id = id;
    }

    public NotedefRole(Integer id, int notedefId, int roleId) {
        this.id = id;
        this.notedefId = notedefId;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNotedefId() {
        return notedefId;
    }

    public void setNotedefId(int notedefId) {
        this.notedefId = notedefId;
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
        if (!(object instanceof NotedefRole)) {
            return false;
        }
        NotedefRole other = (NotedefRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.NotedefRole[id=" + id  +" roleid=" +roleId + " permissions=" + permissions +"]";
    }

}
