/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "survey_indicator")
@NamedQueries({
    @NamedQuery(name = "SurveyIndicator.findAll", query = "SELECT s FROM SurveyIndicator s"),
    @NamedQuery(name = "SurveyIndicator.findById", query = "SELECT s FROM SurveyIndicator s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyIndicator.findByName", query = "SELECT s FROM SurveyIndicator s WHERE s.name = :name"),
    @NamedQuery(name = "SurveyIndicator.findByAnswerType", query = "SELECT s FROM SurveyIndicator s WHERE s.answerType = :answerType"),
    @NamedQuery(name = "SurveyIndicator.findByAnswerTypeId", query = "SELECT s FROM SurveyIndicator s WHERE s.answerTypeId = :answerTypeId"),
    @NamedQuery(name = "SurveyIndicator.findByReferenceId", query = "SELECT s FROM SurveyIndicator s WHERE s.referenceId = :referenceId"),
    @NamedQuery(name = "SurveyIndicator.findByCreateUserId", query = "SELECT s FROM SurveyIndicator s WHERE s.createUserId = :createUserId"),
    @NamedQuery(name = "SurveyIndicator.findByCreateTime", query = "SELECT s FROM SurveyIndicator s WHERE s.createTime = :createTime"),
    @NamedQuery(name = "SurveyIndicator.findByReusable", query = "SELECT s FROM SurveyIndicator s WHERE s.reusable = :reusable"),
    @NamedQuery(name = "SurveyIndicator.findByOriginalIndicatorId", query = "SELECT s FROM SurveyIndicator s WHERE s.originalIndicatorId = :originalIndicatorId"),
    @NamedQuery(name = "SurveyIndicator.findByLanguageId", query = "SELECT s FROM SurveyIndicator s WHERE s.languageId = :languageId"),
    @NamedQuery(name = "SurveyIndicator.findByCreatorOrgId", query = "SELECT s FROM SurveyIndicator s WHERE s.creatorOrgId = :creatorOrgId"),
    @NamedQuery(name = "SurveyIndicator.findByOwnerOrgId", query = "SELECT s FROM SurveyIndicator s WHERE s.ownerOrgId = :ownerOrgId"),
    @NamedQuery(name = "SurveyIndicator.findByVisibility", query = "SELECT s FROM SurveyIndicator s WHERE s.visibility = :visibility"),
    @NamedQuery(name = "SurveyIndicator.findByState", query = "SELECT s FROM SurveyIndicator s WHERE s.state = :state"),
    @NamedQuery(name = "SurveyIndicator.findByStatus", query = "SELECT s FROM SurveyIndicator s WHERE s.status = :status"),
    @NamedQuery(name = "SurveyIndicator.findByDeleteTime", query = "SELECT s FROM SurveyIndicator s WHERE s.deleteTime = :deleteTime"),
    @NamedQuery(name = "SurveyIndicator.findByDeleteUserId", query = "SELECT s FROM SurveyIndicator s WHERE s.deleteUserId = :deleteUserId"),
    @NamedQuery(name = "SurveyIndicator.findByImportId", query = "SELECT s FROM SurveyIndicator s WHERE s.importId = :importId"),
    @NamedQuery(name = "SurveyIndicator.findByParentIndicatorId", query = "SELECT s FROM SurveyIndicator s WHERE s.parentIndicatorId = :parentIndicatorId")})
public class SurveyIndicator implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Lob
    @Column(name = "question")
    private String question;
    @Basic(optional = false)
    @Column(name = "answer_type")
    private short answerType;
    @Basic(optional = false)
    @Column(name = "answer_type_id")
    private int answerTypeId;
    @Basic(optional = false)
    @Column(name = "reference_id")
    private int referenceId;
    @Lob
    @Column(name = "tip")
    private String tip;
    @Column(name = "create_user_id")
    private Integer createUserId;
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "reusable")
    private Boolean reusable;
    @Column(name = "original_indicator_id")
    private Integer originalIndicatorId;
    @Basic(optional = false)
    @Column(name = "language_id")
    private int languageId;
    @Basic(optional = false)
    @Column(name = "creator_org_id")
    private int creatorOrgId;
    @Basic(optional = false)
    @Column(name = "owner_org_id")
    private int ownerOrgId;
    @Basic(optional = false)
    @Column(name = "visibility")
    private short visibility;
    @Basic(optional = false)
    @Column(name = "state")
    private short state;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "delete_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;
    @Column(name = "delete_user_id")
    private Integer deleteUserId;
    @Column(name = "import_id")
    private Integer importId;
    @Column(name = "parent_indicator_id")
    private Integer parentIndicatorId;

    public SurveyIndicator() {
    }

    public SurveyIndicator(Integer id) {
        this.id = id;
    }

    public SurveyIndicator(Integer id, String name, String question, short answerType, int answerTypeId, int referenceId, int languageId, int creatorOrgId, int ownerOrgId, short visibility, short state, short status) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.answerType = answerType;
        this.answerTypeId = answerTypeId;
        this.referenceId = referenceId;
        this.languageId = languageId;
        this.creatorOrgId = creatorOrgId;
        this.ownerOrgId = ownerOrgId;
        this.visibility = visibility;
        this.state = state;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public short getAnswerType() {
        return answerType;
    }

    public void setAnswerType(short answerType) {
        this.answerType = answerType;
    }

    public int getAnswerTypeId() {
        return answerTypeId;
    }

    public void setAnswerTypeId(int answerTypeId) {
        this.answerTypeId = answerTypeId;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getReusable() {
        return reusable;
    }

    public void setReusable(Boolean reusable) {
        this.reusable = reusable;
    }

    public Integer getOriginalIndicatorId() {
        return originalIndicatorId;
    }

    public void setOriginalIndicatorId(Integer originalIndicatorId) {
        this.originalIndicatorId = originalIndicatorId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getCreatorOrgId() {
        return creatorOrgId;
    }

    public void setCreatorOrgId(int creatorOrgId) {
        this.creatorOrgId = creatorOrgId;
    }

    public int getOwnerOrgId() {
        return ownerOrgId;
    }

    public void setOwnerOrgId(int ownerOrgId) {
        this.ownerOrgId = ownerOrgId;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getDeleteUserId() {
        return deleteUserId;
    }

    public void setDeleteUserId(Integer deleteUserId) {
        this.deleteUserId = deleteUserId;
    }

    public Integer getImportId() {
        return importId;
    }

    public void setImportId(Integer importId) {
        this.importId = importId;
    }

    public Integer getParentIndicatorId() {
        return parentIndicatorId;
    }

    public void setParentIndicatorId(Integer parentIndicatorId) {
        this.parentIndicatorId = parentIndicatorId;
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
        if (!(object instanceof SurveyIndicator)) {
            return false;
        }
        SurveyIndicator other = (SurveyIndicator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyIndicator[id=" + id + "]";
    }

}
