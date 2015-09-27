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
@Table(name = "answer_type_float")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerTypeFloat.findAll", query = "SELECT a FROM AnswerTypeFloat a"),
    @NamedQuery(name = "AnswerTypeFloat.findById", query = "SELECT a FROM AnswerTypeFloat a WHERE a.id = :id"),
    @NamedQuery(name = "AnswerTypeFloat.findByMinValue", query = "SELECT a FROM AnswerTypeFloat a WHERE a.minValue = :minValue"),
    @NamedQuery(name = "AnswerTypeFloat.findByMaxValue", query = "SELECT a FROM AnswerTypeFloat a WHERE a.maxValue = :maxValue"),
    @NamedQuery(name = "AnswerTypeFloat.findByDefaultValue", query = "SELECT a FROM AnswerTypeFloat a WHERE a.defaultValue = :defaultValue")})
public class AnswerTypeFloat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "min_value")
    private float minValue;
    @Basic(optional = false)
    @Column(name = "max_value")
    private float maxValue;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "default_value")
    private Float defaultValue;
    @Lob
    @Column(name = "criteria")
    private String criteria;

    public AnswerTypeFloat() {
    }

    public AnswerTypeFloat(Integer id) {
        this.id = id;
    }

    public AnswerTypeFloat(Integer id, float minValue, float maxValue) {
        this.id = id;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public Float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
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
        if (!(object instanceof AnswerTypeFloat)) {
            return false;
        }
        AnswerTypeFloat other = (AnswerTypeFloat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerTypeFloat[ id=" + id + " ]";
    }
    
}
