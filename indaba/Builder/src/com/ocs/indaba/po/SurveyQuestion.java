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
@Table(name = "survey_question")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyQuestion.findAll", query = "SELECT s FROM SurveyQuestion s"),
    @NamedQuery(name = "SurveyQuestion.findById", query = "SELECT s FROM SurveyQuestion s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyQuestion.findByName", query = "SELECT s FROM SurveyQuestion s WHERE s.name = :name"),
    @NamedQuery(name = "SurveyQuestion.findBySurveyConfigId", query = "SELECT s FROM SurveyQuestion s WHERE s.surveyConfigId = :surveyConfigId"),
    @NamedQuery(name = "SurveyQuestion.findBySurveyIndicatorId", query = "SELECT s FROM SurveyQuestion s WHERE s.surveyIndicatorId = :surveyIndicatorId"),
    @NamedQuery(name = "SurveyQuestion.findBySurveyCategoryId", query = "SELECT s FROM SurveyQuestion s WHERE s.surveyCategoryId = :surveyCategoryId"),
    @NamedQuery(name = "SurveyQuestion.findByPublicName", query = "SELECT s FROM SurveyQuestion s WHERE s.publicName = :publicName"),
    @NamedQuery(name = "SurveyQuestion.findByDefaultAnswerId", query = "SELECT s FROM SurveyQuestion s WHERE s.defaultAnswerId = :defaultAnswerId"),
    @NamedQuery(name = "SurveyQuestion.findByWeight", query = "SELECT s FROM SurveyQuestion s WHERE s.weight = :weight")})
public class SurveyQuestion implements Serializable {
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
    @Column(name = "survey_config_id")
    private int surveyConfigId;
    @Basic(optional = false)
    @Column(name = "survey_indicator_id")
    private int surveyIndicatorId;
    @Basic(optional = false)
    @Column(name = "survey_category_id")
    private int surveyCategoryId;
    @Basic(optional = false)
    @Column(name = "public_name")
    private String publicName;
    @Column(name = "default_answer_id")
    private Integer defaultAnswerId;
    @Column(name = "weight")
    private Integer weight;

    public SurveyQuestion() {
    }

    public SurveyQuestion(Integer id) {
        this.id = id;
    }

    public SurveyQuestion(Integer id, String name, int surveyConfigId, int surveyIndicatorId, int surveyCategoryId, String publicName) {
        this.id = id;
        this.name = name;
        this.surveyConfigId = surveyConfigId;
        this.surveyIndicatorId = surveyIndicatorId;
        this.surveyCategoryId = surveyCategoryId;
        this.publicName = publicName;
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

    public int getSurveyCategoryId() {
        return surveyCategoryId;
    }

    public void setSurveyCategoryId(int surveyCategoryId) {
        this.surveyCategoryId = surveyCategoryId;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public Integer getDefaultAnswerId() {
        return defaultAnswerId;
    }

    public void setDefaultAnswerId(Integer defaultAnswerId) {
        this.defaultAnswerId = defaultAnswerId;
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
        if (!(object instanceof SurveyQuestion)) {
            return false;
        }
        SurveyQuestion other = (SurveyQuestion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyQuestion[ id=" + id + " ]";
    }
    
}
