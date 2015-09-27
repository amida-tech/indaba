/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.po;

import java.io.Serializable;
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

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "widget")
@NamedQueries({
    @NamedQuery(name = "Widget.findAll", query = "SELECT w FROM Widget w"),
    @NamedQuery(name = "Widget.findById", query = "SELECT w FROM Widget w WHERE w.id = :id"),
    @NamedQuery(name = "Widget.findByTechName", query = "SELECT w FROM Widget w WHERE w.techName = :techName"),
    @NamedQuery(name = "Widget.findByDisplayName", query = "SELECT w FROM Widget w WHERE w.displayName = :displayName"),
    @NamedQuery(name = "Widget.findByAuthor", query = "SELECT w FROM Widget w WHERE w.author = :author"),
    @NamedQuery(name = "Widget.findByOrgId", query = "SELECT w FROM Widget w WHERE w.orgId = :orgId"),
    @NamedQuery(name = "Widget.findByVersion", query = "SELECT w FROM Widget w WHERE w.version = :version"),
    @NamedQuery(name = "Widget.findByVisibility", query = "SELECT w FROM Widget w WHERE w.visibility = :visibility"),
    @NamedQuery(name = "Widget.findByTargetUrl", query = "SELECT w FROM Widget w WHERE w.targetUrl = :targetUrl"),
    @NamedQuery(name = "Widget.findByIconFileName", query = "SELECT w FROM Widget w WHERE w.iconFileName = :iconFileName"),
    @NamedQuery(name = "Widget.findByContentTypes", query = "SELECT w FROM Widget w WHERE w.contentTypes = :contentTypes"),
    @NamedQuery(name = "Widget.findByConfigType", query = "SELECT w FROM Widget w WHERE w.configType = :configType")})
public class Widget implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tech_name")
    private String techName;
    @Basic(optional = false)
    @Column(name = "display_name")
    private String displayName;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "author")
    private String author;
    @Basic(optional = false)
    @Column(name = "org_id")
    private int orgId;
    @Basic(optional = false)
    @Column(name = "version")
    private String version;
    @Basic(optional = false)
    @Column(name = "visibility")
    private short visibility;
    @Basic(optional = false)
    @Column(name = "target_url")
    private String targetUrl;
    @Basic(optional = false)
    @Column(name = "icon_file_name")
    private String iconFileName;
    @Lob
    @Column(name = "params")
    private String params;
    @Basic(optional = false)
    @Column(name = "content_types")
    private int contentTypes;
    @Basic(optional = false)
    @Column(name = "config_type")
    private int configType;

    public Widget() {
    }

    public Widget(Integer id) {
        this.id = id;
    }

    public Widget(Integer id, String techName, String displayName, int orgId, String version, short visibility, String targetUrl, String iconFileName, int contentTypes, int configType) {
        this.id = id;
        this.techName = techName;
        this.displayName = displayName;
        this.orgId = orgId;
        this.version = version;
        this.visibility = visibility;
        this.targetUrl = targetUrl;
        this.iconFileName = iconFileName;
        this.contentTypes = contentTypes;
        this.configType = configType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getIconFileName() {
        return iconFileName;
    }

    public void setIconFileName(String iconFileName) {
        this.iconFileName = iconFileName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(int contentTypes) {
        this.contentTypes = contentTypes;
    }

    public int getConfigType() {
        return configType;
    }

    public void setConfigType(int configType) {
        this.configType = configType;
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
        if (!(object instanceof Widget)) {
            return false;
        }
        Widget other = (Widget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.Widget[id=" + id + "]";
    }

}
