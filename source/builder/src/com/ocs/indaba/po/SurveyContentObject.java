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
@Table(name = "survey_content_object")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyContentObject.findAll", query = "SELECT s FROM SurveyContentObject s"),
    @NamedQuery(name = "SurveyContentObject.findById", query = "SELECT s FROM SurveyContentObject s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyContentObject.findByContentHeaderId", query = "SELECT s FROM SurveyContentObject s WHERE s.contentHeaderId = :contentHeaderId"),
    @NamedQuery(name = "SurveyContentObject.findBySurveyConfigId", query = "SELECT s FROM SurveyContentObject s WHERE s.surveyConfigId = :surveyConfigId")})
public class SurveyContentObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "content_header_id")
    private int contentHeaderId;
    @Basic(optional = false)
    @Column(name = "survey_config_id")
    private int surveyConfigId;

    public SurveyContentObject() {
    }

    public SurveyContentObject(Integer id) {
        this.id = id;
    }

    public SurveyContentObject(Integer id, int contentHeaderId, int surveyConfigId) {
        this.id = id;
        this.contentHeaderId = contentHeaderId;
        this.surveyConfigId = surveyConfigId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getContentHeaderId() {
        return contentHeaderId;
    }

    public void setContentHeaderId(int contentHeaderId) {
        this.contentHeaderId = contentHeaderId;
    }

    public int getSurveyConfigId() {
        return surveyConfigId;
    }

    public void setSurveyConfigId(int surveyConfigId) {
        this.surveyConfigId = surveyConfigId;
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
        if (!(object instanceof SurveyContentObject)) {
            return false;
        }
        SurveyContentObject other = (SurveyContentObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyContentObject[ id=" + id + " ]";
    }
    
}
