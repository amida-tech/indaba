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
@Table(name = "survey_answer_attachment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyAnswerAttachment.findAll", query = "SELECT s FROM SurveyAnswerAttachment s"),
    @NamedQuery(name = "SurveyAnswerAttachment.findById", query = "SELECT s FROM SurveyAnswerAttachment s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyAnswerAttachment.findBySurveyAnswerId", query = "SELECT s FROM SurveyAnswerAttachment s WHERE s.surveyAnswerId = :surveyAnswerId"),
    @NamedQuery(name = "SurveyAnswerAttachment.findByAttachmentId", query = "SELECT s FROM SurveyAnswerAttachment s WHERE s.attachmentId = :attachmentId")})
public class SurveyAnswerAttachment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_answer_id")
    private int surveyAnswerId;
    @Basic(optional = false)
    @Column(name = "attachment_id")
    private int attachmentId;

    public SurveyAnswerAttachment() {
    }

    public SurveyAnswerAttachment(Integer id) {
        this.id = id;
    }

    public SurveyAnswerAttachment(Integer id, int surveyAnswerId, int attachmentId) {
        this.id = id;
        this.surveyAnswerId = surveyAnswerId;
        this.attachmentId = attachmentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyAnswerId() {
        return surveyAnswerId;
    }

    public void setSurveyAnswerId(int surveyAnswerId) {
        this.surveyAnswerId = surveyAnswerId;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
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
        if (!(object instanceof SurveyAnswerAttachment)) {
            return false;
        }
        SurveyAnswerAttachment other = (SurveyAnswerAttachment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyAnswerAttachment[ id=" + id + " ]";
    }
    
}
