/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

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

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "survey_question_visit")
@NamedQueries({
    @NamedQuery(name = "SurveyQuestionVisit.findAll", query = "SELECT s FROM SurveyQuestionVisit s"),
    @NamedQuery(name = "SurveyQuestionVisit.findById", query = "SELECT s FROM SurveyQuestionVisit s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyQuestionVisit.findByHorseId", query = "SELECT s FROM SurveyQuestionVisit s WHERE s.horseId = :horseId"),
    @NamedQuery(name = "SurveyQuestionVisit.findBySurveyQuestionId", query = "SELECT s FROM SurveyQuestionVisit s WHERE s.surveyQuestionId = :surveyQuestionId"),
    @NamedQuery(name = "SurveyQuestionVisit.findByUserId", query = "SELECT s FROM SurveyQuestionVisit s WHERE s.userId = :userId"),
    @NamedQuery(name = "SurveyQuestionVisit.findByLastVisitTime", query = "SELECT s FROM SurveyQuestionVisit s WHERE s.lastVisitTime = :lastVisitTime")})
public class SurveyQuestionVisit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "horse_id")
    private int horseId;
    @Basic(optional = false)
    @Column(name = "survey_question_id")
    private int surveyQuestionId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "last_visit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastVisitTime;

    public SurveyQuestionVisit() {
    }

    public SurveyQuestionVisit(Integer id) {
        this.id = id;
    }

    public SurveyQuestionVisit(Integer id, int horseId, int surveyQuestionId, int userId, Date lastVisitTime) {
        this.id = id;
        this.horseId = horseId;
        this.surveyQuestionId = surveyQuestionId;
        this.userId = userId;
        this.lastVisitTime = lastVisitTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getSurveyQuestionId() {
        return surveyQuestionId;
    }

    public void setSurveyQuestionId(int surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
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
        if (!(object instanceof SurveyQuestionVisit)) {
            return false;
        }
        SurveyQuestionVisit other = (SurveyQuestionVisit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyQuestionVisit[id=" + id + "]";
    }

}
