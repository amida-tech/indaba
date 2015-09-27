/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "spr_component")
@NamedQueries({
    @NamedQuery(name = "SprComponent.findAll", query = "SELECT s FROM SprComponent s"),
    @NamedQuery(name = "SprComponent.findById", query = "SELECT s FROM SprComponent s WHERE s.id = :id"),
    @NamedQuery(name = "SprComponent.findBySurveyPeerReviewId", query = "SELECT s FROM SprComponent s WHERE s.surveyPeerReviewId = :surveyPeerReviewId"),
    @NamedQuery(name = "SprComponent.findByComponentIndicatorId", query = "SELECT s FROM SprComponent s WHERE s.componentIndicatorId = :componentIndicatorId"),
    @NamedQuery(name = "SprComponent.findByAnswerObjectId", query = "SELECT s FROM SprComponent s WHERE s.answerObjectId = :answerObjectId")})
public class SprComponent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_peer_review_id")
    private int surveyPeerReviewId;
    @Basic(optional = false)
    @Column(name = "component_indicator_id")
    private int componentIndicatorId;
    @Column(name = "answer_object_id")
    private Integer answerObjectId;

    public SprComponent() {
    }

    public SprComponent(Integer id) {
        this.id = id;
    }

    public SprComponent(Integer id, int surveyPeerReviewId, int componentIndicatorId) {
        this.id = id;
        this.surveyPeerReviewId = surveyPeerReviewId;
        this.componentIndicatorId = componentIndicatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyPeerReviewId() {
        return surveyPeerReviewId;
    }

    public void setSurveyPeerReviewId(int surveyPeerReviewId) {
        this.surveyPeerReviewId = surveyPeerReviewId;
    }

    public int getComponentIndicatorId() {
        return componentIndicatorId;
    }

    public void setComponentIndicatorId(int componentIndicatorId) {
        this.componentIndicatorId = componentIndicatorId;
    }

    public Integer getAnswerObjectId() {
        return answerObjectId;
    }

    public void setAnswerObjectId(Integer answerObjectId) {
        this.answerObjectId = answerObjectId;
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
        if (!(object instanceof SprComponent)) {
            return false;
        }
        SprComponent other = (SprComponent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SprComponent[id=" + id + "]";
    }

}
