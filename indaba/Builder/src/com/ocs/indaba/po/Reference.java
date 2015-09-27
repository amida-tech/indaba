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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "reference")
@NamedQueries({
    @NamedQuery(name = "Reference.findAll", query = "SELECT r FROM Reference r"),
    @NamedQuery(name = "Reference.findById", query = "SELECT r FROM Reference r WHERE r.id = :id"),
    @NamedQuery(name = "Reference.findByName", query = "SELECT r FROM Reference r WHERE r.name = :name"),
    @NamedQuery(name = "Reference.findByChoiceType", query = "SELECT r FROM Reference r WHERE r.choiceType = :choiceType"),
    @NamedQuery(name = "Reference.findByClassification", query = "SELECT r FROM Reference r WHERE r.classification = :classification"),
    @NamedQuery(name = "Reference.findByImportId", query = "SELECT r FROM Reference r WHERE r.importId = :importId")})
public class Reference implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "choice_type")
    private short choiceType;
    @Column(name = "classification")
    private Integer classification;
    @Column(name = "import_id")
    private Integer importId;

    public Reference() {
    }

    public Reference(Integer id) {
        this.id = id;
    }

    public Reference(Integer id, String name, String description, short choiceType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.choiceType = choiceType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(short choiceType) {
        this.choiceType = choiceType;
    }

    public Integer getClassification() {
        return classification;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public Integer getImportId() {
        return importId;
    }

    public void setImportId(Integer importId) {
        this.importId = importId;
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
        if (!(object instanceof Reference)) {
            return false;
        }
        Reference other = (Reference) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Reference[id=" + id + "]";
    }

}
