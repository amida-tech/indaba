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
@Table(name = "atc_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtcChoice.findAll", query = "SELECT a FROM AtcChoice a"),
    @NamedQuery(name = "AtcChoice.findById", query = "SELECT a FROM AtcChoice a WHERE a.id = :id"),
    @NamedQuery(name = "AtcChoice.findByAnswerTypeChoiceId", query = "SELECT a FROM AtcChoice a WHERE a.answerTypeChoiceId = :answerTypeChoiceId"),
    @NamedQuery(name = "AtcChoice.findByLabel", query = "SELECT a FROM AtcChoice a WHERE a.label = :label"),
    @NamedQuery(name = "AtcChoice.findByScore", query = "SELECT a FROM AtcChoice a WHERE a.score = :score"),
    @NamedQuery(name = "AtcChoice.findByWeight", query = "SELECT a FROM AtcChoice a WHERE a.weight = :weight"),
    @NamedQuery(name = "AtcChoice.findByMask", query = "SELECT a FROM AtcChoice a WHERE a.mask = :mask"),
    @NamedQuery(name = "AtcChoice.findByDefaultSelected", query = "SELECT a FROM AtcChoice a WHERE a.defaultSelected = :defaultSelected"),
    @NamedQuery(name = "AtcChoice.findByUseScore", query = "SELECT a FROM AtcChoice a WHERE a.useScore = :useScore")})
public class AtcChoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "answer_type_choice_id")
    private int answerTypeChoiceId;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @Column(name = "score")
    private Integer score;
    @Lob
    @Column(name = "criteria")
    private String criteria;
    @Basic(optional = false)
    @Column(name = "weight")
    private int weight;
    @Basic(optional = false)
    @Column(name = "mask")
    private long mask;
    @Basic(optional = false)
    @Column(name = "default_selected")
    private boolean defaultSelected;
    @Basic(optional = false)
    @Column(name = "use_score")
    private boolean useScore;

    public AtcChoice() {
    }

    public AtcChoice(Integer id) {
        this.id = id;
    }

    public AtcChoice(Integer id, int answerTypeChoiceId, String label, int weight, long mask, boolean defaultSelected, boolean useScore) {
        this.id = id;
        this.answerTypeChoiceId = answerTypeChoiceId;
        this.label = label;
        this.weight = weight;
        this.mask = mask;
        this.defaultSelected = defaultSelected;
        this.useScore = useScore;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAnswerTypeChoiceId() {
        return answerTypeChoiceId;
    }

    public void setAnswerTypeChoiceId(int answerTypeChoiceId) {
        this.answerTypeChoiceId = answerTypeChoiceId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getMask() {
        return mask;
    }

    public void setMask(long mask) {
        this.mask = mask;
    }

    public boolean getDefaultSelected() {
        return defaultSelected;
    }

    public void setDefaultSelected(boolean defaultSelected) {
        this.defaultSelected = defaultSelected;
    }

    public boolean getUseScore() {
        return useScore;
    }

    public void setUseScore(boolean useScore) {
        this.useScore = useScore;
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
        if (!(object instanceof AtcChoice)) {
            return false;
        }
        AtcChoice other = (AtcChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AtcChoice[ id=" + id + " ]";
    }
    
}
