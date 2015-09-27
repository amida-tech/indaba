/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "survey_answer_attachment_version")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findAll", query = "SELECT s FROM SurveyAnswerAttachmentVersion s"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findById", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findBySurveyAnswerVersionId", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.surveyAnswerVersionId = :surveyAnswerVersionId"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findByName", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.name = :name"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findBySize", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.size = :size"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findByType", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.type = :type"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findByNote", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.note = :note"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findByFilePath", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.filePath = :filePath"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findByUpdateTime", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.updateTime = :updateTime"),
    @NamedQuery(name = "SurveyAnswerAttachmentVersion.findByUserId", query = "SELECT s FROM SurveyAnswerAttachmentVersion s WHERE s.userId = :userId")})
public class SurveyAnswerAttachmentVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "survey_answer_version_id")
    private int surveyAnswerVersionId;
    @Column(name = "name")
    private String name;
    @Column(name = "size")
    private Integer size;
    @Column(name = "type")
    private String type;
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "user_id")
    private Integer userId;

    public SurveyAnswerAttachmentVersion() {
    }

    public SurveyAnswerAttachmentVersion(Integer id) {
        this.id = id;
    }

    public SurveyAnswerAttachmentVersion(Integer id, int surveyAnswerVersionId, String filePath) {
        this.id = id;
        this.surveyAnswerVersionId = surveyAnswerVersionId;
        this.filePath = filePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSurveyAnswerVersionId() {
        return surveyAnswerVersionId;
    }

    public void setSurveyAnswerVersionId(int surveyAnswerVersionId) {
        this.surveyAnswerVersionId = surveyAnswerVersionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        if (!(object instanceof SurveyAnswerAttachmentVersion)) {
            return false;
        }
        SurveyAnswerAttachmentVersion other = (SurveyAnswerAttachmentVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyAnswerAttachmentVersion[ id=" + id + " ]";
    }
    
}
