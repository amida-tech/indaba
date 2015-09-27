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
@Table(name = "sc_contributor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScContributor.findAll", query = "SELECT s FROM ScContributor s"),
    @NamedQuery(name = "ScContributor.findById", query = "SELECT s FROM ScContributor s WHERE s.id = :id"),
    @NamedQuery(name = "ScContributor.findBySurveyConfigId", query = "SELECT s FROM ScContributor s WHERE s.surveyConfigId = :surveyConfigId"),
    @NamedQuery(name = "ScContributor.findByOrgId", query = "SELECT s FROM ScContributor s WHERE s.orgId = :orgId")})
public class ScContributor implements Serializable {
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
    @Column(name = "org_id")
    private int orgId;

    public ScContributor() {
    }

    public ScContributor(Integer id) {
        this.id = id;
    }

    public ScContributor(Integer id, int surveyConfigId, int orgId) {
        this.id = id;
        this.surveyConfigId = surveyConfigId;
        this.orgId = orgId;
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

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
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
        if (!(object instanceof ScContributor)) {
            return false;
        }
        ScContributor other = (ScContributor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ScContributor[ id=" + id + " ]";
    }
    
}
