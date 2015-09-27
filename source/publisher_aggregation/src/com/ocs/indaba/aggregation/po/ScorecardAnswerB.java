/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.po;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jiangjeff
 */
@Entity
@Table(name = "scorecard_answer_b")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScorecardAnswerB.findAll", query = "SELECT s FROM ScorecardAnswerB s"),
    @NamedQuery(name = "ScorecardAnswerB.findById", query = "SELECT s FROM ScorecardAnswerB s WHERE s.id = :id"),
    @NamedQuery(name = "ScorecardAnswerB.findByScorecardId", query = "SELECT s FROM ScorecardAnswerB s WHERE s.scorecardId = :scorecardId"),
    @NamedQuery(name = "ScorecardAnswerB.findByQuestionId", query = "SELECT s FROM ScorecardAnswerB s WHERE s.questionId = :questionId"),
    @NamedQuery(name = "ScorecardAnswerB.findByIndicatorId", query = "SELECT s FROM ScorecardAnswerB s WHERE s.indicatorId = :indicatorId"),
    @NamedQuery(name = "ScorecardAnswerB.findByDataType", query = "SELECT s FROM ScorecardAnswerB s WHERE s.dataType = :dataType"),
    @NamedQuery(name = "ScorecardAnswerB.findByScore", query = "SELECT s FROM ScorecardAnswerB s WHERE s.score = :score")})
public class ScorecardAnswerB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "scorecard_id")
    private int scorecardId;
    @Basic(optional = false)
    @Column(name = "question_id")
    private int questionId;
    @Basic(optional = false)
    @Column(name = "indicator_id")
    private int indicatorId;
    @Basic(optional = false)
    @Column(name = "data_type")
    private short dataType;
    @Basic(optional = false)
    @Lob
    @Column(name = "value")
    private String value;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    private BigDecimal score;

    public ScorecardAnswerB() {
    }

    public ScorecardAnswerB(Integer id) {
        this.id = id;
    }

    public ScorecardAnswerB(Integer id, int scorecardId, int questionId, int indicatorId, short dataType, String value) {
        this.id = id;
        this.scorecardId = scorecardId;
        this.questionId = questionId;
        this.indicatorId = indicatorId;
        this.dataType = dataType;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getScorecardId() {
        return scorecardId;
    }

    public void setScorecardId(int scorecardId) {
        this.scorecardId = scorecardId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public short getDataType() {
        return dataType;
    }

    public void setDataType(short dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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
        if (!(object instanceof ScorecardAnswerB)) {
            return false;
        }
        ScorecardAnswerB other = (ScorecardAnswerB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.ScorecardAnswerB[ id=" + id + " ]";
    }
    
}
