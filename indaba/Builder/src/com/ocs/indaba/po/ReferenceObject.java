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
@Table(name = "reference_object")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReferenceObject.findAll", query = "SELECT r FROM ReferenceObject r"),
    @NamedQuery(name = "ReferenceObject.findById", query = "SELECT r FROM ReferenceObject r WHERE r.id = :id"),
    @NamedQuery(name = "ReferenceObject.findByReferenceId", query = "SELECT r FROM ReferenceObject r WHERE r.referenceId = :referenceId"),
    @NamedQuery(name = "ReferenceObject.findByChoices", query = "SELECT r FROM ReferenceObject r WHERE r.choices = :choices")})
public class ReferenceObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "reference_id")
    private int referenceId;
    @Basic(optional = false)
    @Lob
    @Column(name = "source_description")
    private String sourceDescription;
    @Lob
    @Column(name = "comments")
    private String comments;
    @Column(name = "choices")
    private long choices;

    public ReferenceObject() {
    }

    public ReferenceObject(Integer id) {
        this.id = id;
    }

    public ReferenceObject(Integer id, int referenceId, String sourceDescription) {
        this.id = id;
        this.referenceId = referenceId;
        this.sourceDescription = sourceDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getChoices() {
        return choices;
    }

    public void setChoices(long choices) {
        this.choices = choices;
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
        if (!(object instanceof ReferenceObject)) {
            return false;
        }
        ReferenceObject other = (ReferenceObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ReferenceObject[ id=" + id + " ]";
    }
    
}
