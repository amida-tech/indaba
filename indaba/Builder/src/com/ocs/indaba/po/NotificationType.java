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
@Table(name = "notification_type")
@NamedQueries({
    @NamedQuery(name = "NotificationType.findAll", query = "SELECT n FROM NotificationType n"),
    @NamedQuery(name = "NotificationType.findById", query = "SELECT n FROM NotificationType n WHERE n.id = :id"),
    @NamedQuery(name = "NotificationType.findByName", query = "SELECT n FROM NotificationType n WHERE n.name = :name"),
    @NamedQuery(name = "NotificationType.findByDescription", query = "SELECT n FROM NotificationType n WHERE n.description = :description"),
    @NamedQuery(name = "NotificationType.findByLabel", query = "SELECT n FROM NotificationType n WHERE n.label = :label"),
    @NamedQuery(name = "NotificationType.findByProjectCustomizable", query = "SELECT n FROM NotificationType n WHERE n.projectCustomizable = :projectCustomizable"),
    @NamedQuery(name = "NotificationType.findByCategory", query = "SELECT n FROM NotificationType n WHERE n.category = :category"),
    @NamedQuery(name = "NotificationType.findByTemplateType", query = "SELECT n FROM NotificationType n WHERE n.templateType = :templateType")})
public class NotificationType implements Serializable {
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
    @Column(name = "label")
    private String label;
    @Column(name = "project_customizable")
    private Boolean projectCustomizable;
    @Column(name = "category")
    private Integer category;
    @Basic(optional = false)
    @Column(name = "template_type")
    private short templateType;

    public NotificationType() {
    }

    public NotificationType(Integer id) {
        this.id = id;
    }

    public NotificationType(Integer id, String name, short templateType) {
        this.id = id;
        this.name = name;
        this.templateType = templateType;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getProjectCustomizable() {
        return projectCustomizable;
    }

    public void setProjectCustomizable(Boolean projectCustomizable) {
        this.projectCustomizable = projectCustomizable;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public short getTemplateType() {
        return templateType;
    }

    public void setTemplateType(short templateType) {
        this.templateType = templateType;
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
        if (!(object instanceof NotificationType)) {
            return false;
        }
        NotificationType other = (NotificationType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.NotificationType[id=" + id + "]";
    }

}
