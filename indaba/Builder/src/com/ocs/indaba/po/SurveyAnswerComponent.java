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
@Table(name = "survey_answer_component")
@NamedQueries({
    @NamedQuery(name = "SurveyAnswerComponent.findAll", query = "SELECT s FROM SurveyAnswerComponent s"),
    @NamedQuery(name = "SurveyAnswerComponent.findById", query = "SELECT s FROM SurveyAnswerComponent s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyAnswerComponent.findBySurveyAnswerId", query = "SELECT s FROM SurveyAnswerComponent s WHERE s.surveyAnswerId = :surveyAnswerId"),
    @NamedQuery(name = "SurveyAnswerComponent.findByComponentIndicatorId", query = "SELECT s FROM SurveyAnswerComponent s WHERE s.componentIndicatorId = :componentIndicatorId"),
    @NamedQuery(name = "SurveyAnswerComponent.findByAnswerObjectId", query = "SELECT s FROM SurveyAnswerComponent s WHERE s.answerObjectId = :answerObjectId"),
    @NamedQuery(name = "SurveyAnswerComponent.findByAnswerTime", query = "SELECT s FROM SurveyAnswerComponent s WHERE s.answerTime = :answerTime"),
    @NamedQuery(name = "SurveyAnswerComponent.findByAnswerUserId", query = "SELECT s FROM SurveyAnswerComponent s WHERE s.answerUserId = :answerUserId")})
public class SurveyAnswerComponent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_answer_id")
    private int surveyAnswerId;
    @Basic(optional = false)
    @Column(name = "component_indicator_id")
    private int componentIndicatorId;
    @Column(name = "answer_object_id")
    private Integer answerObjectId;
    @Column(name = "answer_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date answerTime;
    @Column(name = "answer_user_id")
    private Integer answerUserId;

    public SurveyAnswerComponent() {
    }

    public SurveyAnswerComponent(Integer id) {
        this.id = id;
    }

    public SurveyAnswerComponent(Integer id, int surveyAnswerId, int componentIndicatorId) {
        this.id = id;
        this.surveyAnswerId = surveyAnswerId;
        this.componentIndicatorId = componentIndicatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyAnswerId() {
        return surveyAnswerId;
    }

    public void setSurveyAnswerId(int surveyAnswerId) {
        this.surveyAnswerId = surveyAnswerId;
    }

    public int getComponentIndicatorId() {
        return componentIndicatorId;
    }

    public void setComponentIndicatorId(int componentIndicatorId) {
        this.componentIndicatorId = componentIndicatorId;
    }

    public Integer getAnswerObjectId() {
        return answerObjectId;
    }

    public void setAnswerObjectId(Integer answerObjectId) {
        this.answerObjectId = answerObjectId;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public Integer getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(Integer answerUserId) {
        this.answerUserId = answerUserId;
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
        if (!(object instanceof SurveyAnswerComponent)) {
            return false;
        }
        SurveyAnswerComponent other = (SurveyAnswerComponent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyAnswerComponent[id=" + id + "]";
    }

}
