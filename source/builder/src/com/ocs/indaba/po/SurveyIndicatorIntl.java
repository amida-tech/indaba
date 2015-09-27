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
@Table(name = "survey_indicator_intl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyIndicatorIntl.findAll", query = "SELECT s FROM SurveyIndicatorIntl s"),
    @NamedQuery(name = "SurveyIndicatorIntl.findById", query = "SELECT s FROM SurveyIndicatorIntl s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyIndicatorIntl.findBySurveyIndicatorId", query = "SELECT s FROM SurveyIndicatorIntl s WHERE s.surveyIndicatorId = :surveyIndicatorId"),
    @NamedQuery(name = "SurveyIndicatorIntl.findByLanguageId", query = "SELECT s FROM SurveyIndicatorIntl s WHERE s.languageId = :languageId")})
public class SurveyIndicatorIntl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_indicator_id")
    private int surveyIndicatorId;
    @Basic(optional = false)
    @Column(name = "language_id")
    private int languageId;
    @Basic(optional = false)
    @Lob
    @Column(name = "question")
    private String question;
    @Lob
    @Column(name = "tip")
    private String tip;

    public SurveyIndicatorIntl() {
    }

    public SurveyIndicatorIntl(Integer id) {
        this.id = id;
    }

    public SurveyIndicatorIntl(Integer id, int surveyIndicatorId, int languageId, String question) {
        this.id = id;
        this.surveyIndicatorId = surveyIndicatorId;
        this.languageId = languageId;
        this.question = question;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyIndicatorId() {
        return surveyIndicatorId;
    }

    public void setSurveyIndicatorId(int surveyIndicatorId) {
        this.surveyIndicatorId = surveyIndicatorId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
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
        if (!(object instanceof SurveyIndicatorIntl)) {
            return false;
        }
        SurveyIndicatorIntl other = (SurveyIndicatorIntl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyIndicatorIntl[ id=" + id + " ]";
    }
    
}
