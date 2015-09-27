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
@Table(name = "noteobj_version")
@NamedQueries({
    @NamedQuery(name = "NoteobjVersion.findAll", query = "SELECT n FROM NoteobjVersion n"),
    @NamedQuery(name = "NoteobjVersion.findById", query = "SELECT n FROM NoteobjVersion n WHERE n.id = :id"),
    @NamedQuery(name = "NoteobjVersion.findByNotedefId", query = "SELECT n FROM NoteobjVersion n WHERE n.notedefId = :notedefId"),
    @NamedQuery(name = "NoteobjVersion.findByContentVersionId", query = "SELECT n FROM NoteobjVersion n WHERE n.contentVersionId = :contentVersionId"),
    @NamedQuery(name = "NoteobjVersion.findBySurveyQuestionId", query = "SELECT n FROM NoteobjVersion n WHERE n.surveyQuestionId = :surveyQuestionId")})
public class NoteobjVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "notedef_id")
    private int notedefId;
    @Basic(optional = false)
    @Column(name = "content_version_id")
    private int contentVersionId;
    @Basic(optional = false)
    @Column(name = "survey_question_id")
    private int surveyQuestionId;

    public NoteobjVersion() {
    }

    public NoteobjVersion(Integer id) {
        this.id = id;
    }

    public NoteobjVersion(Integer id, int notedefId, int contentVersionId, int surveyQuestionId) {
        this.id = id;
        this.notedefId = notedefId;
        this.contentVersionId = contentVersionId;
        this.surveyQuestionId = surveyQuestionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNotedefId() {
        return notedefId;
    }

    public void setNotedefId(int notedefId) {
        this.notedefId = notedefId;
    }

    public int getContentVersionId() {
        return contentVersionId;
    }

    public void setContentVersionId(int contentVersionId) {
        this.contentVersionId = contentVersionId;
    }

    public int getSurveyQuestionId() {
        return surveyQuestionId;
    }

    public void setSurveyQuestionId(int surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
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
        if (!(object instanceof NoteobjVersion)) {
            return false;
        }
        NoteobjVersion other = (NoteobjVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.NoteobjVersion[id=" + id + "]";
    }

}
