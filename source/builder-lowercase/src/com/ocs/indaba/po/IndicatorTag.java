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
@Table(name = "indicator_tag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorTag.findAll", query = "SELECT i FROM IndicatorTag i"),
    @NamedQuery(name = "IndicatorTag.findById", query = "SELECT i FROM IndicatorTag i WHERE i.id = :id"),
    @NamedQuery(name = "IndicatorTag.findBySurveyIndicatorId", query = "SELECT i FROM IndicatorTag i WHERE i.surveyIndicatorId = :surveyIndicatorId"),
    @NamedQuery(name = "IndicatorTag.findByItagsId", query = "SELECT i FROM IndicatorTag i WHERE i.itagsId = :itagsId")})
public class IndicatorTag implements Serializable {
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
    @Column(name = "itags_id")
    private int itagsId;

    public IndicatorTag() {
    }

    public IndicatorTag(Integer id) {
        this.id = id;
    }

    public IndicatorTag(Integer id, int surveyIndicatorId, int itagsId) {
        this.id = id;
        this.surveyIndicatorId = surveyIndicatorId;
        this.itagsId = itagsId;
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

    public int getItagsId() {
        return itagsId;
    }

    public void setItagsId(int itagsId) {
        this.itagsId = itagsId;
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
        if (!(object instanceof IndicatorTag)) {
            return false;
        }
        IndicatorTag other = (IndicatorTag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.IndicatorTag[ id=" + id + " ]";
    }
    
}
