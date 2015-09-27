/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.po;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jiangjeff
 */
@Entity
@Table(name = "datapoint")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Datapoint.findAll", query = "SELECT d FROM Datapoint d"),
    @NamedQuery(name = "Datapoint.findById", query = "SELECT d FROM Datapoint d WHERE d.id = :id"),
    @NamedQuery(name = "Datapoint.findByWorksetId", query = "SELECT d FROM Datapoint d WHERE d.worksetId = :worksetId"),
    @NamedQuery(name = "Datapoint.findByName", query = "SELECT d FROM Datapoint d WHERE d.name = :name"),
    @NamedQuery(name = "Datapoint.findByDescription", query = "SELECT d FROM Datapoint d WHERE d.description = :description"),
    @NamedQuery(name = "Datapoint.findByAggrMethodId", query = "SELECT d FROM Datapoint d WHERE d.aggrMethodId = :aggrMethodId"),
    @NamedQuery(name = "Datapoint.findByHolePolicy", query = "SELECT d FROM Datapoint d WHERE d.holePolicy = :holePolicy"),
    @NamedQuery(name = "Datapoint.findByShortName", query = "SELECT d FROM Datapoint d WHERE d.shortName = :shortName")})
public class Datapoint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workset_id")
    private int worksetId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "aggr_method_id")
    private int aggrMethodId;
    @Basic(optional = false)
    @Column(name = "hole_policy")
    private short holePolicy;
    @Basic(optional = false)
    @Column(name = "short_name")
    private String shortName;

    public Datapoint() {
    }

    public Datapoint(Integer id) {
        this.id = id;
    }

    public Datapoint(Integer id, int worksetId, String name, int aggrMethodId, short holePolicy, String shortName) {
        this.id = id;
        this.worksetId = worksetId;
        this.name = name;
        this.aggrMethodId = aggrMethodId;
        this.holePolicy = holePolicy;
        this.shortName = shortName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorksetId() {
        return worksetId;
    }

    public void setWorksetId(int worksetId) {
        this.worksetId = worksetId;
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

    public int getAggrMethodId() {
        return aggrMethodId;
    }

    public void setAggrMethodId(int aggrMethodId) {
        this.aggrMethodId = aggrMethodId;
    }

    public short getHolePolicy() {
        return holePolicy;
    }

    public void setHolePolicy(short holePolicy) {
        this.holePolicy = holePolicy;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
        if (!(object instanceof Datapoint)) {
            return false;
        }
        Datapoint other = (Datapoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.Datapoint[ id=" + id + " ]";
    }
    
}
