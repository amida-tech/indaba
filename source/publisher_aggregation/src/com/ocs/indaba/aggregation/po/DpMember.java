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
@Table(name = "dp_member")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DpMember.findAll", query = "SELECT d FROM DpMember d"),
    @NamedQuery(name = "DpMember.findById", query = "SELECT d FROM DpMember d WHERE d.id = :id"),
    @NamedQuery(name = "DpMember.findByDatapointId", query = "SELECT d FROM DpMember d WHERE d.datapointId = :datapointId"),
    @NamedQuery(name = "DpMember.findByIndicatorInstanceId", query = "SELECT d FROM DpMember d WHERE d.indicatorInstanceId = :indicatorInstanceId"),
    @NamedQuery(name = "DpMember.findByDpId", query = "SELECT d FROM DpMember d WHERE d.dpId = :dpId"),
    @NamedQuery(name = "DpMember.findByWeight", query = "SELECT d FROM DpMember d WHERE d.weight = :weight")})
public class DpMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "datapoint_id")
    private int datapointId;
    @Column(name = "indicator_instance_id")
    private Integer indicatorInstanceId;
    @Column(name = "dp_id")
    private Integer dpId;
    @Column(name = "weight")
    private Integer weight;

    public DpMember() {
    }

    public DpMember(Integer id) {
        this.id = id;
    }

    public DpMember(Integer id, int datapointId) {
        this.id = id;
        this.datapointId = datapointId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDatapointId() {
        return datapointId;
    }

    public void setDatapointId(int datapointId) {
        this.datapointId = datapointId;
    }

    public Integer getIndicatorInstanceId() {
        return indicatorInstanceId;
    }

    public void setIndicatorInstanceId(Integer indicatorInstanceId) {
        this.indicatorInstanceId = indicatorInstanceId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
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
        if (!(object instanceof DpMember)) {
            return false;
        }
        DpMember other = (DpMember) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.DpMember[ id=" + id + " ]";
    }
    
}
