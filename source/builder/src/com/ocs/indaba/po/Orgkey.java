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
@Table(name = "orgkey")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orgkey.findAll", query = "SELECT o FROM Orgkey o"),
    @NamedQuery(name = "Orgkey.findById", query = "SELECT o FROM Orgkey o WHERE o.id = :id"),
    @NamedQuery(name = "Orgkey.findByOrganizationId", query = "SELECT o FROM Orgkey o WHERE o.organizationId = :organizationId"),
    @NamedQuery(name = "Orgkey.findByVersion", query = "SELECT o FROM Orgkey o WHERE o.version = :version"),
    @NamedQuery(name = "Orgkey.findByHashAlgorithm", query = "SELECT o FROM Orgkey o WHERE o.hashAlgorithm = :hashAlgorithm"),
    @NamedQuery(name = "Orgkey.findByIssueTime", query = "SELECT o FROM Orgkey o WHERE o.issueTime = :issueTime"),
    @NamedQuery(name = "Orgkey.findByIssueUserId", query = "SELECT o FROM Orgkey o WHERE o.issueUserId = :issueUserId"),
    @NamedQuery(name = "Orgkey.findByEffectiveTime", query = "SELECT o FROM Orgkey o WHERE o.effectiveTime = :effectiveTime"),
    @NamedQuery(name = "Orgkey.findByValidDays", query = "SELECT o FROM Orgkey o WHERE o.validDays = :validDays"),
    @NamedQuery(name = "Orgkey.findByStatus", query = "SELECT o FROM Orgkey o WHERE o.status = :status"),
    @NamedQuery(name = "Orgkey.findByRevokeTime", query = "SELECT o FROM Orgkey o WHERE o.revokeTime = :revokeTime"),
    @NamedQuery(name = "Orgkey.findByRevokeUserId", query = "SELECT o FROM Orgkey o WHERE o.revokeUserId = :revokeUserId"),
    @NamedQuery(name = "Orgkey.findByRevokeReason", query = "SELECT o FROM Orgkey o WHERE o.revokeReason = :revokeReason"),
    @NamedQuery(name = "Orgkey.findByRenewTime", query = "SELECT o FROM Orgkey o WHERE o.renewTime = :renewTime"),
    @NamedQuery(name = "Orgkey.findByRenewUserId", query = "SELECT o FROM Orgkey o WHERE o.renewUserId = :renewUserId"),
    @NamedQuery(name = "Orgkey.findByData", query = "SELECT o FROM Orgkey o WHERE o.data = :data")})
public class Orgkey implements Serializable {
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
    @Column(name = "version")
    private int version;
    @Basic(optional = false)
    @Column(name = "hash_algorithm")
    private String hashAlgorithm;
    @Basic(optional = false)
    @Column(name = "issue_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime;
    @Basic(optional = false)
    @Column(name = "issue_user_id")
    private int issueUserId;
    @Basic(optional = false)
    @Column(name = "effective_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveTime;
    @Basic(optional = false)
    @Column(name = "valid_days")
    private int validDays;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "revoke_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date revokeTime;
    @Column(name = "revoke_user_id")
    private Integer revokeUserId;
    @Column(name = "revoke_reason")
    private String revokeReason;
    @Column(name = "renew_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date renewTime;
    @Column(name = "renew_user_id")
    private Integer renewUserId;
    @Basic(optional = false)
    @Column(name = "data")
    private String data;

    public Orgkey() {
    }

    public Orgkey(Integer id) {
        this.id = id;
    }

    public Orgkey(Integer id, int organizationId, int version, String hashAlgorithm, Date issueTime, int issueUserId, Date effectiveTime, int validDays, short status, String data) {
        this.id = id;
        this.organizationId = organizationId;
        this.version = version;
        this.hashAlgorithm = hashAlgorithm;
        this.issueTime = issueTime;
        this.issueUserId = issueUserId;
        this.effectiveTime = effectiveTime;
        this.validDays = validDays;
        this.status = status;
        this.data = data;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public int getIssueUserId() {
        return issueUserId;
    }

    public void setIssueUserId(int issueUserId) {
        this.issueUserId = issueUserId;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public int getValidDays() {
        return validDays;
    }

    public void setValidDays(int validDays) {
        this.validDays = validDays;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getRevokeTime() {
        return revokeTime;
    }

    public void setRevokeTime(Date revokeTime) {
        this.revokeTime = revokeTime;
    }

    public Integer getRevokeUserId() {
        return revokeUserId;
    }

    public void setRevokeUserId(Integer revokeUserId) {
        this.revokeUserId = revokeUserId;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }

    public Date getRenewTime() {
        return renewTime;
    }

    public void setRenewTime(Date renewTime) {
        this.renewTime = renewTime;
    }

    public Integer getRenewUserId() {
        return renewUserId;
    }

    public void setRenewUserId(Integer renewUserId) {
        this.renewUserId = renewUserId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        if (!(object instanceof Orgkey)) {
            return false;
        }
        Orgkey other = (Orgkey) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Orgkey[ id=" + id + " ]";
    }
    
}
