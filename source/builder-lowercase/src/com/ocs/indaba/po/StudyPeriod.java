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
@Table(name = "study_period")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudyPeriod.findAll", query = "SELECT s FROM StudyPeriod s"),
    @NamedQuery(name = "StudyPeriod.findById", query = "SELECT s FROM StudyPeriod s WHERE s.id = :id"),
    @NamedQuery(name = "StudyPeriod.findByName", query = "SELECT s FROM StudyPeriod s WHERE s.name = :name"),
    @NamedQuery(name = "StudyPeriod.findByDescription", query = "SELECT s FROM StudyPeriod s WHERE s.description = :description")})
public class StudyPeriod implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    public StudyPeriod() {
    }

    public StudyPeriod(Integer id) {
        this.id = id;
    }

    public StudyPeriod(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudyPeriod)) {
            return false;
        }
        StudyPeriod other = (StudyPeriod) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.StudyPeriod[ id=" + id + " ]";
    }
    
}
