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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "sc_indicator")
@NamedQueries({
    @NamedQuery(name = "ScIndicator.findAll", query = "SELECT s FROM ScIndicator s"),
    @NamedQuery(name = "ScIndicator.findById", query = "SELECT s FROM ScIndicator s WHERE s.id = :id"),
    @NamedQuery(name = "ScIndicator.findBySurveyConfigId", query = "SELECT s FROM ScIndicator s WHERE s.surveyConfigId = :surveyConfigId"),
    @NamedQuery(name = "ScIndicator.findBySurveyIndicatorId", query = "SELECT s FROM ScIndicator s WHERE s.surveyIndicatorId = :surveyIndicatorId")})
public class ScIndicator implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_config_id")
    private int surveyConfigId;
    @Basic(optional = false)
    @Column(name = "survey_indicator_id")
    private int surveyIndicatorId;

    public ScIndicator() {
    }

    public ScIndicator(Integer id) {
        this.id = id;
    }

    public ScIndicator(Integer id, int surveyConfigId, int surveyIndicatorId) {
        this.id = id;
        this.surveyConfigId = surveyConfigId;
        this.surveyIndicatorId = surveyIndicatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyConfigId() {
        return surveyConfigId;
    }

    public void setSurveyConfigId(int surveyConfigId) {
        this.surveyConfigId = surveyConfigId;
    }

    public int getSurveyIndicatorId() {
        return surveyIndicatorId;
    }

    public void setSurveyIndicatorId(int surveyIndicatorId) {
        this.surveyIndicatorId = surveyIndicatorId;
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
        if (!(object instanceof ScIndicator)) {
            return false;
        }
        ScIndicator other = (ScIndicator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ScIndicator[id=" + id + "]";
    }

}
