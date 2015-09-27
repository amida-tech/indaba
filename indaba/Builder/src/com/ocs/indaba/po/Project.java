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
@Table(name = "project")
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
    @NamedQuery(name = "Project.findById", query = "SELECT p FROM Project p WHERE p.id = :id"),
    @NamedQuery(name = "Project.findByOrganizationId", query = "SELECT p FROM Project p WHERE p.organizationId = :organizationId"),
    @NamedQuery(name = "Project.findByCodeName", query = "SELECT p FROM Project p WHERE p.codeName = :codeName"),
    @NamedQuery(name = "Project.findByDescription", query = "SELECT p FROM Project p WHERE p.description = :description"),
    @NamedQuery(name = "Project.findByOwnerUserId", query = "SELECT p FROM Project p WHERE p.ownerUserId = :ownerUserId"),
    @NamedQuery(name = "Project.findByCreationTime", query = "SELECT p FROM Project p WHERE p.creationTime = :creationTime"),
    @NamedQuery(name = "Project.findByAccessMatrixId", query = "SELECT p FROM Project p WHERE p.accessMatrixId = :accessMatrixId"),
    @NamedQuery(name = "Project.findByViewMatrixId", query = "SELECT p FROM Project p WHERE p.viewMatrixId = :viewMatrixId"),
    @NamedQuery(name = "Project.findByStartTime", query = "SELECT p FROM Project p WHERE p.startTime = :startTime"),
    @NamedQuery(name = "Project.findByStudyPeriodId", query = "SELECT p FROM Project p WHERE p.studyPeriodId = :studyPeriodId"),
    @NamedQuery(name = "Project.findByStatus", query = "SELECT p FROM Project p WHERE p.status = :status"),
    @NamedQuery(name = "Project.findByLogoPath", query = "SELECT p FROM Project p WHERE p.logoPath = :logoPath"),
    @NamedQuery(name = "Project.findByMsgboardId", query = "SELECT p FROM Project p WHERE p.msgboardId = :msgboardId"),
    @NamedQuery(name = "Project.findByAdminUserId", query = "SELECT p FROM Project p WHERE p.adminUserId = :adminUserId"),
    @NamedQuery(name = "Project.findBySponsorLogos", query = "SELECT p FROM Project p WHERE p.sponsorLogos = :sponsorLogos"),
    @NamedQuery(name = "Project.findByIsActive", query = "SELECT p FROM Project p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "Project.findByCloseTime", query = "SELECT p FROM Project p WHERE p.closeTime = :closeTime"),
    @NamedQuery(name = "Project.findByVisibility", query = "SELECT p FROM Project p WHERE p.visibility = :visibility"),
    @NamedQuery(name = "Project.findByImportId", query = "SELECT p FROM Project p WHERE p.importId = :importId"),
    @NamedQuery(name = "Project.findByReportUrl", query = "SELECT p FROM Project p WHERE p.reportUrl = :reportUrl")})
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "organization_id")
    private int organizationId;
    @Basic(optional = false)
    @Column(name = "code_name")
    private String codeName;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "owner_user_id")
    private int ownerUserId;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Basic(optional = false)
    @Column(name = "access_matrix_id")
    private int accessMatrixId;
    @Basic(optional = false)
    @Column(name = "view_matrix_id")
    private int viewMatrixId;
    @Basic(optional = false)
    @Column(name = "start_time")
    @Temporal(TemporalType.DATE)
    private Date startTime;
    @Basic(optional = false)
    @Column(name = "study_period_id")
    private int studyPeriodId;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @Column(name = "logo_path")
    private String logoPath;
    @Column(name = "msgboard_id")
    private Integer msgboardId;
    @Basic(optional = false)
    @Column(name = "admin_user_id")
    private int adminUserId;
    @Column(name = "sponsor_logos")
    private String sponsorLogos;
    @Basic(optional = false)
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "close_time")
    @Temporal(TemporalType.DATE)
    private Date closeTime;
    @Basic(optional = false)
    @Column(name = "visibility")
    private short visibility;
    @Column(name = "import_id")
    private Integer importId;
    @Column(name = "report_url")
    private String reportUrl;
    @Column(name = "analytics_url")
    private String analyticsUrl;

    public Project() {
    }

    public Project(Integer id) {
        this.id = id;
    }

    public Project(Integer id, int organizationId, String codeName, String description, int ownerUserId, int accessMatrixId, int viewMatrixId, Date startTime, int studyPeriodId, int status, int adminUserId, boolean isActive, short visibility) {
        this.id = id;
        this.organizationId = organizationId;
        this.codeName = codeName;
        this.description = description;
        this.ownerUserId = ownerUserId;
        this.accessMatrixId = accessMatrixId;
        this.viewMatrixId = viewMatrixId;
        this.startTime = startTime;
        this.studyPeriodId = studyPeriodId;
        this.status = status;
        this.adminUserId = adminUserId;
        this.isActive = isActive;
        this.visibility = visibility;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getAccessMatrixId() {
        return accessMatrixId;
    }

    public void setAccessMatrixId(int accessMatrixId) {
        this.accessMatrixId = accessMatrixId;
    }

    public int getViewMatrixId() {
        return viewMatrixId;
    }

    public void setViewMatrixId(int viewMatrixId) {
        this.viewMatrixId = viewMatrixId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getStudyPeriodId() {
        return studyPeriodId;
    }

    public void setStudyPeriodId(int studyPeriodId) {
        this.studyPeriodId = studyPeriodId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Integer getMsgboardId() {
        return msgboardId;
    }

    public void setMsgboardId(Integer msgboardId) {
        this.msgboardId = msgboardId;
    }

    public int getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(int adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getSponsorLogos() {
        return sponsorLogos;
    }

    public void setSponsorLogos(String sponsorLogos) {
        this.sponsorLogos = sponsorLogos;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public Integer getImportId() {
        return importId;
    }

    public void setImportId(Integer importId) {
        this.importId = importId;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getAnalyticsUrl() {
        return analyticsUrl;
    }

    public void setAnalyticsUrl(String url) {
        this.analyticsUrl = url;
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Project[id=" + id + "]";
    }

}
