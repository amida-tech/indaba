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
@Table(name = "answer_type_text")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerTypeText.findAll", query = "SELECT a FROM AnswerTypeText a"),
    @NamedQuery(name = "AnswerTypeText.findById", query = "SELECT a FROM AnswerTypeText a WHERE a.id = :id"),
    @NamedQuery(name = "AnswerTypeText.findByMinChars", query = "SELECT a FROM AnswerTypeText a WHERE a.minChars = :minChars"),
    @NamedQuery(name = "AnswerTypeText.findByMaxChars", query = "SELECT a FROM AnswerTypeText a WHERE a.maxChars = :maxChars")})
public class AnswerTypeText implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "min_chars")
    private int minChars;
    @Basic(optional = false)
    @Column(name = "max_chars")
    private int maxChars;
    @Lob
    @Column(name = "criteria")
    private String criteria;

    public AnswerTypeText() {
    }

    public AnswerTypeText(Integer id) {
        this.id = id;
    }

    public AnswerTypeText(Integer id, int minChars, int maxChars) {
        this.id = id;
        this.minChars = minChars;
        this.maxChars = maxChars;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMinChars() {
        return minChars;
    }

    public void setMinChars(int minChars) {
        this.minChars = minChars;
    }

    public int getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
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
        if (!(object instanceof AnswerTypeText)) {
            return false;
        }
        AnswerTypeText other = (AnswerTypeText) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerTypeText[ id=" + id + " ]";
    }
    
}
