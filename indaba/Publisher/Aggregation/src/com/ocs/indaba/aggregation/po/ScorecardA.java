/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jiangjeff
 */
@Entity
@Table(name = "scorecard_a")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScorecardA.findAll", query = "SELECT s FROM ScorecardA s"),
    @NamedQuery(name = "ScorecardA.findById", query = "SELECT s FROM ScorecardA s WHERE s.id = :id"),
    @NamedQuery(name = "ScorecardA.findByScorecardId", query = "SELECT s FROM ScorecardA s WHERE s.scorecardId = :scorecardId"),
    @NamedQuery(name = "ScorecardA.findByContentHeaderId", query = "SELECT s FROM ScorecardA s WHERE s.contentHeaderId = :contentHeaderId"),
    @NamedQuery(name = "ScorecardA.findByTargetId", query = "SELECT s FROM ScorecardA s WHERE s.targetId = :targetId"),
    @NamedQuery(name = "ScorecardA.findByProductId", query = "SELECT s FROM ScorecardA s WHERE s.productId = :productId"),
    @NamedQuery(name = "ScorecardA.findByProjectId", query = "SELECT s FROM ScorecardA s WHERE s.projectId = :projectId"),
    @NamedQuery(name = "ScorecardA.findByOrgId", query = "SELECT s FROM ScorecardA s WHERE s.orgId = :orgId"),
    @NamedQuery(name = "ScorecardA.findByStudyPeriodId", query = "SELECT s FROM ScorecardA s WHERE s.studyPeriodId = :studyPeriodId"),
    @NamedQuery(name = "ScorecardA.findBySurveyConfigId", query = "SELECT s FROM ScorecardA s WHERE s.surveyConfigId = :surveyConfigId"),
    @NamedQuery(name = "ScorecardA.findByStatus", query = "SELECT s FROM ScorecardA s WHERE s.status = :status"),
    @NamedQuery(name = "ScorecardA.findByLastUpdateTime", query = "SELECT s FROM ScorecardA s WHERE s.lastUpdateTime = :lastUpdateTime")})
public class ScorecardA implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "scorecard_id")
    private int scorecardId;
    @Basic(optional = false)
    @Column(name = "content_header_id")
    private int contentHeaderId;
    @Basic(optional = false)
    @Column(name = "target_id")
    private int targetId;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;
    @Basic(optional = false)
    @Column(name = "project_id")
    private int projectId;
    @Basic(optional = false)
    @Column(name = "org_id")
    private int orgId;
    @Basic(optional = false)
    @Column(name = "study_period_id")
    private int studyPeriodId;
    @Basic(optional = false)
    @Column(name = "survey_config_id")
    private int surveyConfigId;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Basic(optional = false)
    @Column(name = "last_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public ScorecardA() {
    }

    public ScorecardA(Integer id) {
        this.id = id;
    }

    public ScorecardA(Integer id, int scorecardId, int contentHeaderId, int targetId, int productId, int projectId, int orgId, int studyPeriodId, int surveyConfigId, short status, Date lastUpdateTime) {
        this.id = id;
        this.scorecardId = scorecardId;
        this.contentHeaderId = contentHeaderId;
        this.targetId = targetId;
        this.productId = productId;
        this.projectId = projectId;
        this.orgId = orgId;
        this.studyPeriodId = studyPeriodId;
        this.surveyConfigId = surveyConfigId;
        this.status = status;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getScorecardId() {
        return scorecardId;
    }

    public void setScorecardId(int scorecardId) {
        this.scorecardId = scorecardId;
    }

    public int getContentHeaderId() {
        return contentHeaderId;
    }

    public void setContentHeaderId(int contentHeaderId) {
        this.contentHeaderId = contentHeaderId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getStudyPeriodId() {
        return studyPeriodId;
    }

    public void setStudyPeriodId(int studyPeriodId) {
        this.studyPeriodId = studyPeriodId;
    }

    public int getSurveyConfigId() {
        return surveyConfigId;
    }

    public void setSurveyConfigId(int surveyConfigId) {
        this.surveyConfigId = surveyConfigId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
        if (!(object instanceof ScorecardA)) {
            return false;
        }
        ScorecardA other = (ScorecardA) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.ScorecardA[ id=" + id + " ]";
    }
    
}
