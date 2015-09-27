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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jiangjeff
 */
@Entity
@Table(name = "otis_value_a")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtisValueA.findAll", query = "SELECT o FROM OtisValueA o"),
    @NamedQuery(name = "OtisValueA.findById", query = "SELECT o FROM OtisValueA o WHERE o.id = :id"),
    @NamedQuery(name = "OtisValueA.findByOrgId", query = "SELECT o FROM OtisValueA o WHERE o.orgId = :orgId"),
    @NamedQuery(name = "OtisValueA.findByTargetId", query = "SELECT o FROM OtisValueA o WHERE o.targetId = :targetId"),
    @NamedQuery(name = "OtisValueA.findByIndicatorId", query = "SELECT o FROM OtisValueA o WHERE o.indicatorId = :indicatorId"),
    @NamedQuery(name = "OtisValueA.findByStudyPeriodId", query = "SELECT o FROM OtisValueA o WHERE o.studyPeriodId = :studyPeriodId"),
    @NamedQuery(name = "OtisValueA.findByDataType", query = "SELECT o FROM OtisValueA o WHERE o.dataType = :dataType"),
    @NamedQuery(name = "OtisValueA.findByScore", query = "SELECT o FROM OtisValueA o WHERE o.score = :score")})
public class OtisValueA implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "org_id")
    private int orgId;
    @Basic(optional = false)
    @Column(name = "target_id")
    private int targetId;
    @Basic(optional = false)
    @Column(name = "indicator_id")
    private int indicatorId;
    @Basic(optional = false)
    @Column(name = "study_period_id")
    private int studyPeriodId;
    @Basic(optional = false)
    @Column(name = "data_type")
    private short dataType;
    @Basic(optional = false)
    @Lob
    @Column(name = "value")
    private String value;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    private BigDecimal score;

    public OtisValueA() {
    }

    public OtisValueA(Integer id) {
        this.id = id;
    }

    public OtisValueA(Integer id, int orgId, int targetId, int indicatorId, int studyPeriodId, short dataType, String value) {
        this.id = id;
        this.orgId = orgId;
        this.targetId = targetId;
        this.indicatorId = indicatorId;
        this.studyPeriodId = studyPeriodId;
        this.dataType = dataType;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public int getStudyPeriodId() {
        return studyPeriodId;
    }

    public void setStudyPeriodId(int studyPeriodId) {
        this.studyPeriodId = studyPeriodId;
    }

    public short getDataType() {
        return dataType;
    }

    public void setDataType(short dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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
        if (!(object instanceof OtisValueA)) {
            return false;
        }
        OtisValueA other = (OtisValueA) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.OtisValueA[ id=" + id + " ]";
    }
    
}
