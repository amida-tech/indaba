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
@Table(name = "case_object")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CaseObject.findAll", query = "SELECT c FROM CaseObject c"),
    @NamedQuery(name = "CaseObject.findById", query = "SELECT c FROM CaseObject c WHERE c.id = :id"),
    @NamedQuery(name = "CaseObject.findByCasesId", query = "SELECT c FROM CaseObject c WHERE c.casesId = :casesId"),
    @NamedQuery(name = "CaseObject.findByObjectType", query = "SELECT c FROM CaseObject c WHERE c.objectType = :objectType"),
    @NamedQuery(name = "CaseObject.findByObjectId", query = "SELECT c FROM CaseObject c WHERE c.objectId = :objectId")})
public class CaseObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cases_id")
    private int casesId;
    @Basic(optional = false)
    @Column(name = "object_type")
    private short objectType;
    @Basic(optional = false)
    @Column(name = "object_id")
    private int objectId;

    public CaseObject() {
    }

    public CaseObject(Integer id) {
        this.id = id;
    }

    public CaseObject(Integer id, int casesId, short objectType, int objectId) {
        this.id = id;
        this.casesId = casesId;
        this.objectType = objectType;
        this.objectId = objectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCasesId() {
        return casesId;
    }

    public void setCasesId(int casesId) {
        this.casesId = casesId;
    }

    public short getObjectType() {
        return objectType;
    }

    public void setObjectType(short objectType) {
        this.objectType = objectType;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
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
        if (!(object instanceof CaseObject)) {
            return false;
        }
        CaseObject other = (CaseObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.CaseObject[ id=" + id + " ]";
    }
    
}
