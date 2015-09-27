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
@Table(name = "notedef_user")
@NamedQueries({
    @NamedQuery(name = "NotedefUser.findAll", query = "SELECT n FROM NotedefUser n"),
    @NamedQuery(name = "NotedefUser.findById", query = "SELECT n FROM NotedefUser n WHERE n.id = :id"),
    @NamedQuery(name = "NotedefUser.findByNotedefId", query = "SELECT n FROM NotedefUser n WHERE n.notedefId = :notedefId"),
    @NamedQuery(name = "NotedefUser.findByUserId", query = "SELECT n FROM NotedefUser n WHERE n.userId = :userId"),
    @NamedQuery(name = "NotedefUser.findByPermissions", query = "SELECT n FROM NotedefUser n WHERE n.permissions = :permissions")})
public class NotedefUser implements Serializable {
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
    @Column(name = "user_id")
    private int userId;
    @Column(name = "permissions")
    private Integer permissions;

    public NotedefUser() {
    }

    public NotedefUser(Integer id) {
        this.id = id;
    }

    public NotedefUser(Integer id, int notedefId, int userId) {
        this.id = id;
        this.notedefId = notedefId;
        this.userId = userId;
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
        if (!(object instanceof NotedefUser)) {
            return false;
        }
        NotedefUser other = (NotedefUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.NotedefUser[id=" + id + " userid="+userId+ " permissions=" + permissions+"]";
    }

}
