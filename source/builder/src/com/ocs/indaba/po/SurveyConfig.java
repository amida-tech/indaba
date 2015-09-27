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
@Table(name = "survey_config")
@NamedQueries({
    @NamedQuery(name = "SurveyConfig.findAll", query = "SELECT s FROM SurveyConfig s"),
    @NamedQuery(name = "SurveyConfig.findById", query = "SELECT s FROM SurveyConfig s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyConfig.findByName", query = "SELECT s FROM SurveyConfig s WHERE s.name = :name"),
    @NamedQuery(name = "SurveyConfig.findByDescription", query = "SELECT s FROM SurveyConfig s WHERE s.description = :description"),
    @NamedQuery(name = "SurveyConfig.findByMoeAlgorithm", query = "SELECT s FROM SurveyConfig s WHERE s.moeAlgorithm = :moeAlgorithm"),
    @NamedQuery(name = "SurveyConfig.findByIsTsc", query = "SELECT s FROM SurveyConfig s WHERE s.isTsc = :isTsc"),
    @NamedQuery(name = "SurveyConfig.findByCreatorOrgId", query = "SELECT s FROM SurveyConfig s WHERE s.creatorOrgId = :creatorOrgId"),
    @NamedQuery(name = "SurveyConfig.findByOwnerOrgId", query = "SELECT s FROM SurveyConfig s WHERE s.ownerOrgId = :ownerOrgId"),
    @NamedQuery(name = "SurveyConfig.findByVisibility", query = "SELECT s FROM SurveyConfig s WHERE s.visibility = :visibility"),
    @NamedQuery(name = "SurveyConfig.findByLanguageId", query = "SELECT s FROM SurveyConfig s WHERE s.languageId = :languageId"),
    @NamedQuery(name = "SurveyConfig.findByStatus", query = "SELECT s FROM SurveyConfig s WHERE s.status = :status"),
    @NamedQuery(name = "SurveyConfig.findByCreateTime", query = "SELECT s FROM SurveyConfig s WHERE s.createTime = :createTime"),
    @NamedQuery(name = "SurveyConfig.findByDeleteTime", query = "SELECT s FROM SurveyConfig s WHERE s.deleteTime = :deleteTime"),
    @NamedQuery(name = "SurveyConfig.findByImportId", query = "SELECT s FROM SurveyConfig s WHERE s.importId = :importId"),
    @NamedQuery(name = "SurveyConfig.findByTipDisplayMethod", query = "SELECT s FROM SurveyConfig s WHERE s.tipDisplayMethod = :tipDisplayMethod")})
public class SurveyConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Lob
    @Column(name = "instructions")
    private String instructions;
    @Basic(optional = false)
    @Column(name = "moe_algorithm")
    private short moeAlgorithm;
    @Column(name = "is_tsc")
    private Boolean isTsc;
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
    @Column(name = "language_id")
    private int languageId;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "delete_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;
    @Column(name = "import_id")
    private Integer importId;
    @Basic(optional = false)
    @Column(name = "tip_display_method")
    private short tipDisplayMethod;

    public SurveyConfig() {
    }

    public SurveyConfig(Integer id) {
        this.id = id;
    }

    public SurveyConfig(Integer id, String name, String instructions, short moeAlgorithm, int creatorOrgId, int ownerOrgId, short visibility, int languageId, short status, Date createTime, short tipDisplayMethod) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.moeAlgorithm = moeAlgorithm;
        this.creatorOrgId = creatorOrgId;
        this.ownerOrgId = ownerOrgId;
        this.visibility = visibility;
        this.languageId = languageId;
        this.status = status;
        this.createTime = createTime;
        this.tipDisplayMethod = tipDisplayMethod;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public short getMoeAlgorithm() {
        return moeAlgorithm;
    }

    public void setMoeAlgorithm(short moeAlgorithm) {
        this.moeAlgorithm = moeAlgorithm;
    }

    public Boolean getIsTsc() {
        return isTsc;
    }

    public void setIsTsc(Boolean isTsc) {
        this.isTsc = isTsc;
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

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getImportId() {
        return importId;
    }

    public void setImportId(Integer importId) {
        this.importId = importId;
    }

    public short getTipDisplayMethod() {
        return tipDisplayMethod;
    }

    public void setTipDisplayMethod(short tipDisplayMethod) {
        this.tipDisplayMethod = tipDisplayMethod;
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
        if (!(object instanceof SurveyConfig)) {
            return false;
        }
        SurveyConfig other = (SurveyConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.SurveyConfig[id=" + id + "]";
    }

}
