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
@Table(name = "survey_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyCategory.findAll", query = "SELECT s FROM SurveyCategory s"),
    @NamedQuery(name = "SurveyCategory.findById", query = "SELECT s FROM SurveyCategory s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyCategory.findBySurveyConfigId", query = "SELECT s FROM SurveyCategory s WHERE s.surveyConfigId = :surveyConfigId"),
    @NamedQuery(name = "SurveyCategory.findByParentCategoryId", query = "SELECT s FROM SurveyCategory s WHERE s.parentCategoryId = :parentCategoryId"),
    @NamedQuery(name = "SurveyCategory.findByName", query = "SELECT s FROM SurveyCategory s WHERE s.name = :name"),
    @NamedQuery(name = "SurveyCategory.findByDescription", query = "SELECT s FROM SurveyCategory s WHERE s.description = :description"),
    @NamedQuery(name = "SurveyCategory.findByLabel", query = "SELECT s FROM SurveyCategory s WHERE s.label = :label"),
    @NamedQuery(name = "SurveyCategory.findByWeight", query = "SELECT s FROM SurveyCategory s WHERE s.weight = :weight")})
public class SurveyCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_config_id")
    private int surveyConfigId;
    @Column(name = "parent_category_id")
    private Integer parentCategoryId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @Lob
    @Column(name = "title")
    private String title;
    @Column(name = "weight")
    private Integer weight;

    public SurveyCategory() {
    }

    public SurveyCategory(Integer id) {
        this.id = id;
    }

    public SurveyCategory(Integer id, int surveyConfigId, String name, String description, String label) {
        this.id = id;
        this.surveyConfigId = surveyConfigId;
        this.name = name;
        this.description = description;
        this.label = label;
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

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        if (!(object instanceof SurveyCategory)) {
            return false;
        }
        SurveyCategory other = (SurveyCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyCategory[ id=" + id + " ]";
    }
    
}
