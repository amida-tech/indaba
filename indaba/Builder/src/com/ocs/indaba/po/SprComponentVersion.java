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
@Table(name = "spr_component_version")
@NamedQueries({
    @NamedQuery(name = "SprComponentVersion.findAll", query = "SELECT s FROM SprComponentVersion s"),
    @NamedQuery(name = "SprComponentVersion.findById", query = "SELECT s FROM SprComponentVersion s WHERE s.id = :id"),
    @NamedQuery(name = "SprComponentVersion.findBySurveyPeerReviewVersionId", query = "SELECT s FROM SprComponentVersion s WHERE s.surveyPeerReviewVersionId = :surveyPeerReviewVersionId"),
    @NamedQuery(name = "SprComponentVersion.findByComponentIndicatorId", query = "SELECT s FROM SprComponentVersion s WHERE s.componentIndicatorId = :componentIndicatorId"),
    @NamedQuery(name = "SprComponentVersion.findByAnswerObjectId", query = "SELECT s FROM SprComponentVersion s WHERE s.answerObjectId = :answerObjectId")})
public class SprComponentVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_peer_review_version_id")
    private int surveyPeerReviewVersionId;
    @Basic(optional = false)
    @Column(name = "component_indicator_id")
    private int componentIndicatorId;
    @Column(name = "answer_object_id")
    private Integer answerObjectId;

    public SprComponentVersion() {
    }

    public SprComponentVersion(Integer id) {
        this.id = id;
    }

    public SprComponentVersion(Integer id, int surveyPeerReviewVersionId, int componentIndicatorId) {
        this.id = id;
        this.surveyPeerReviewVersionId = surveyPeerReviewVersionId;
        this.componentIndicatorId = componentIndicatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyPeerReviewVersionId() {
        return surveyPeerReviewVersionId;
    }

    public void setSurveyPeerReviewVersionId(int surveyPeerReviewVersionId) {
        this.surveyPeerReviewVersionId = surveyPeerReviewVersionId;
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
        if (!(object instanceof SprComponentVersion)) {
            return false;
        }
        SprComponentVersion other = (SprComponentVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SprComponentVersion[id=" + id + "]";
    }

}
