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
@Table(name = "answer_type_integer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerTypeInteger.findAll", query = "SELECT a FROM AnswerTypeInteger a"),
    @NamedQuery(name = "AnswerTypeInteger.findById", query = "SELECT a FROM AnswerTypeInteger a WHERE a.id = :id"),
    @NamedQuery(name = "AnswerTypeInteger.findByMinValue", query = "SELECT a FROM AnswerTypeInteger a WHERE a.minValue = :minValue"),
    @NamedQuery(name = "AnswerTypeInteger.findByMaxValue", query = "SELECT a FROM AnswerTypeInteger a WHERE a.maxValue = :maxValue"),
    @NamedQuery(name = "AnswerTypeInteger.findByDefaultValue", query = "SELECT a FROM AnswerTypeInteger a WHERE a.defaultValue = :defaultValue")})
public class AnswerTypeInteger implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "min_value")
    private int minValue;
    @Basic(optional = false)
    @Column(name = "max_value")
    private int maxValue;
    @Column(name = "default_value")
    private Integer defaultValue;
    @Lob
    @Column(name = "criteria")
    private String criteria;

    public AnswerTypeInteger() {
    }

    public AnswerTypeInteger(Integer id) {
        this.id = id;
    }

    public AnswerTypeInteger(Integer id, int minValue, int maxValue) {
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

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
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
        if (!(object instanceof AnswerTypeInteger)) {
            return false;
        }
        AnswerTypeInteger other = (AnswerTypeInteger) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerTypeInteger[ id=" + id + " ]";
    }
    
}
