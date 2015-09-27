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
@Table(name = "apicall")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Apicall.findAll", query = "SELECT a FROM Apicall a"),
    @NamedQuery(name = "Apicall.findById", query = "SELECT a FROM Apicall a WHERE a.id = :id"),
    @NamedQuery(name = "Apicall.findByCallTime", query = "SELECT a FROM Apicall a WHERE a.callTime = :callTime"),
    @NamedQuery(name = "Apicall.findByFunc", query = "SELECT a FROM Apicall a WHERE a.func = :func"),
    @NamedQuery(name = "Apicall.findByUrl", query = "SELECT a FROM Apicall a WHERE a.url = :url"),
    @NamedQuery(name = "Apicall.findByIpAddr", query = "SELECT a FROM Apicall a WHERE a.ipAddr = :ipAddr"),
    @NamedQuery(name = "Apicall.findByOrganizationId", query = "SELECT a FROM Apicall a WHERE a.organizationId = :organizationId"),
    @NamedQuery(name = "Apicall.findByKeyVersion", query = "SELECT a FROM Apicall a WHERE a.keyVersion = :keyVersion"),
    @NamedQuery(name = "Apicall.findByProductId", query = "SELECT a FROM Apicall a WHERE a.productId = :productId"),
    @NamedQuery(name = "Apicall.findByHorseId", query = "SELECT a FROM Apicall a WHERE a.horseId = :horseId"),
    @NamedQuery(name = "Apicall.findByAuthnCode", query = "SELECT a FROM Apicall a WHERE a.authnCode = :authnCode"),
    @NamedQuery(name = "Apicall.findByAuthzCode", query = "SELECT a FROM Apicall a WHERE a.authzCode = :authzCode")})
public class Apicall implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "call_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date callTime;
    @Basic(optional = false)
    @Column(name = "func")
    private String func;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @Column(name = "ip_addr")
    private String ipAddr;
    @Column(name = "organization_id")
    private Integer organizationId;
    @Column(name = "key_version")
    private Integer keyVersion;
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "horse_id")
    private Integer horseId;
    @Basic(optional = false)
    @Column(name = "authn_code")
    private short authnCode;
    @Column(name = "authz_code")
    private Short authzCode;

    public Apicall() {
    }

    public Apicall(Integer id) {
        this.id = id;
    }

    public Apicall(Integer id, Date callTime, String func, String url, short authnCode) {
        this.id = id;
        this.callTime = callTime;
        this.func = func;
        this.url = url;
        this.authnCode = authnCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(Integer keyVersion) {
        this.keyVersion = keyVersion;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getHorseId() {
        return horseId;
    }

    public void setHorseId(Integer horseId) {
        this.horseId = horseId;
    }

    public short getAuthnCode() {
        return authnCode;
    }

    public void setAuthnCode(short authnCode) {
        this.authnCode = authnCode;
    }

    public Short getAuthzCode() {
        return authzCode;
    }

    public void setAuthzCode(Short authzCode) {
        this.authzCode = authzCode;
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
        if (!(object instanceof Apicall)) {
            return false;
        }
        Apicall other = (Apicall) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Apicall[ id=" + id + " ]";
    }
    
}
