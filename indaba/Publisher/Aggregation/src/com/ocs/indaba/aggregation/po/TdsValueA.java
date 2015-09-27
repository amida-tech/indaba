/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.po;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "tds_value_a")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TdsValueA.findAll", query = "SELECT t FROM TdsValueA t"),
    @NamedQuery(name = "TdsValueA.findById", query = "SELECT t FROM TdsValueA t WHERE t.id = :id"),
    @NamedQuery(name = "TdsValueA.findByDatasetId", query = "SELECT t FROM TdsValueA t WHERE t.datasetId = :datasetId"),
    @NamedQuery(name = "TdsValueA.findByTargetId", query = "SELECT t FROM TdsValueA t WHERE t.targetId = :targetId"),
    @NamedQuery(name = "TdsValueA.findByDatapointId", query = "SELECT t FROM TdsValueA t WHERE t.datapointId = :datapointId"),
    @NamedQuery(name = "TdsValueA.findByStudyPeriodId", query = "SELECT t FROM TdsValueA t WHERE t.studyPeriodId = :studyPeriodId"),
    @NamedQuery(name = "TdsValueA.findByValue", query = "SELECT t FROM TdsValueA t WHERE t.value = :value")})
public class TdsValueA implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "dataset_id")
    private int datasetId;
    @Basic(optional = false)
    @Column(name = "target_id")
    private int targetId;
    @Basic(optional = false)
    @Column(name = "datapoint_id")
    private int datapointId;
    @Basic(optional = false)
    @Column(name = "study_period_id")
    private int studyPeriodId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "value")
    private BigDecimal value;

    public TdsValueA() {
    }

    public TdsValueA(Integer id) {
        this.id = id;
    }

    public TdsValueA(Integer id, int datasetId, int targetId, int datapointId, int studyPeriodId, BigDecimal value) {
        this.id = id;
        this.datasetId = datasetId;
        this.targetId = targetId;
        this.datapointId = datapointId;
        this.studyPeriodId = studyPeriodId;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getDatapointId() {
        return datapointId;
    }

    public void setDatapointId(int datapointId) {
        this.datapointId = datapointId;
    }

    public int getStudyPeriodId() {
        return studyPeriodId;
    }

    public void setStudyPeriodId(int studyPeriodId) {
        this.studyPeriodId = studyPeriodId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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
        if (!(object instanceof TdsValueA)) {
            return false;
        }
        TdsValueA other = (TdsValueA) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.TdsValueA[ id=" + id + " ]";
    }
    
}
