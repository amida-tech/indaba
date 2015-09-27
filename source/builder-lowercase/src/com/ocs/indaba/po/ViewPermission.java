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
@Table(name = "view_permission")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewPermission.findAll", query = "SELECT v FROM ViewPermission v"),
    @NamedQuery(name = "ViewPermission.findById", query = "SELECT v FROM ViewPermission v WHERE v.id = :id"),
    @NamedQuery(name = "ViewPermission.findByViewMatrixId", query = "SELECT v FROM ViewPermission v WHERE v.viewMatrixId = :viewMatrixId"),
    @NamedQuery(name = "ViewPermission.findBySubjectRoleId", query = "SELECT v FROM ViewPermission v WHERE v.subjectRoleId = :subjectRoleId"),
    @NamedQuery(name = "ViewPermission.findByTargetRoleId", query = "SELECT v FROM ViewPermission v WHERE v.targetRoleId = :targetRoleId"),
    @NamedQuery(name = "ViewPermission.findByPermission", query = "SELECT v FROM ViewPermission v WHERE v.permission = :permission")})
public class ViewPermission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "view_matrix_id")
    private int viewMatrixId;
    @Basic(optional = false)
    @Column(name = "subject_role_id")
    private int subjectRoleId;
    @Basic(optional = false)
    @Column(name = "target_role_id")
    private int targetRoleId;
    @Basic(optional = false)
    @Column(name = "permission")
    private short permission;

    public ViewPermission() {
    }

    public ViewPermission(Integer id) {
        this.id = id;
    }

    public ViewPermission(Integer id, int viewMatrixId, int subjectRoleId, int targetRoleId, short permission) {
        this.id = id;
        this.viewMatrixId = viewMatrixId;
        this.subjectRoleId = subjectRoleId;
        this.targetRoleId = targetRoleId;
        this.permission = permission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getViewMatrixId() {
        return viewMatrixId;
    }

    public void setViewMatrixId(int viewMatrixId) {
        this.viewMatrixId = viewMatrixId;
    }

    public int getSubjectRoleId() {
        return subjectRoleId;
    }

    public void setSubjectRoleId(int subjectRoleId) {
        this.subjectRoleId = subjectRoleId;
    }

    public int getTargetRoleId() {
        return targetRoleId;
    }

    public void setTargetRoleId(int targetRoleId) {
        this.targetRoleId = targetRoleId;
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
        if (!(object instanceof ViewPermission)) {
            return false;
        }
        ViewPermission other = (ViewPermission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ViewPermission[ id=" + id + " ]";
    }
    
}
