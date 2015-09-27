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
@Table(name = "config")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Config.findAll", query = "SELECT c FROM Config c"),
    @NamedQuery(name = "Config.findById", query = "SELECT c FROM Config c WHERE c.id = :id"),
    @NamedQuery(name = "Config.findByDefaultLanguageId", query = "SELECT c FROM Config c WHERE c.defaultLanguageId = :defaultLanguageId"),
    @NamedQuery(name = "Config.findByPlatformName", query = "SELECT c FROM Config c WHERE c.platformName = :platformName"),
    @NamedQuery(name = "Config.findByPlatformAdminUserId", query = "SELECT c FROM Config c WHERE c.platformAdminUserId = :platformAdminUserId")})
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "default_language_id")
    private int defaultLanguageId;
    @Basic(optional = false)
    @Column(name = "platform_name")
    private String platformName;
    @Basic(optional = false)
    @Column(name = "platform_admin_user_id")
    private int platformAdminUserId;
    @Basic(optional = false)
    @Column(name = "builder_config")
    private String builderConfig;
    @Basic(optional = false)
    @Column(name = "publisher_config")
    private String publisherConfig;
    @Basic(optional = false)
    @Column(name = "cp_config")
    private String cpConfig;

    public Config() {
    }

    public Config(Integer id) {
        this.id = id;
    }

    public Config(Integer id, int defaultLanguageId, String platformName, int platformAdminUserId) {
        this.id = id;
        this.defaultLanguageId = defaultLanguageId;
        this.platformName = platformName;
        this.platformAdminUserId = platformAdminUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDefaultLanguageId() {
        return defaultLanguageId;
    }

    public void setDefaultLanguageId(int defaultLanguageId) {
        this.defaultLanguageId = defaultLanguageId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public int getPlatformAdminUserId() {
        return platformAdminUserId;
    }

    public void setPlatformAdminUserId(int platformAdminUserId) {
        this.platformAdminUserId = platformAdminUserId;
    }

    public String getBuilderConfig() {
        return builderConfig;
    }

    public void setBuilderConfig(String builderConfig) {
        this.builderConfig = builderConfig;
    }

    public String getPublisherConfig() {
        return publisherConfig;
    }

    public void setPublisherConfig(String publisherConfig) {
        this.publisherConfig = publisherConfig;
    }

    public String getCpConfig() {
        return cpConfig;
    }

    public void setCpConfig(String cpConfig) {
        this.cpConfig = cpConfig;
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
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Config[ id=" + id + " ]";
    }
    
}
