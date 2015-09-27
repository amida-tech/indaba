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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "project_notif")
@NamedQueries({
    @NamedQuery(name = "ProjectNotif.findAll", query = "SELECT p FROM ProjectNotif p"),
    @NamedQuery(name = "ProjectNotif.findById", query = "SELECT p FROM ProjectNotif p WHERE p.id = :id"),
    @NamedQuery(name = "ProjectNotif.findByProjectId", query = "SELECT p FROM ProjectNotif p WHERE p.projectId = :projectId"),
    @NamedQuery(name = "ProjectNotif.findByNotificationTypeId", query = "SELECT p FROM ProjectNotif p WHERE p.notificationTypeId = :notificationTypeId"),
    @NamedQuery(name = "ProjectNotif.findByLanguageId", query = "SELECT p FROM ProjectNotif p WHERE p.languageId = :languageId"),
    @NamedQuery(name = "ProjectNotif.findByName", query = "SELECT p FROM ProjectNotif p WHERE p.name = :name")})
public class ProjectNotif implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "project_id")
    private int projectId;
    @Basic(optional = false)
    @Column(name = "notification_type_id")
    private int notificationTypeId;
    @Column(name = "language_id")
    private Integer languageId;
    @Lob
    @Column(name = "subject_text")
    private String subjectText;
    @Lob
    @Column(name = "body_text")
    private String bodyText;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;

    public ProjectNotif() {
    }

    public ProjectNotif(Integer id) {
        this.id = id;
    }

    public ProjectNotif(Integer id, int projectId, int notificationTypeId) {
        this.id = id;
        this.projectId = projectId;
        this.notificationTypeId = notificationTypeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(int notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getSubjectText() {
        return subjectText;
    }

    public void setSubjectText(String subjectText) {
        this.subjectText = subjectText;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectNotif)) {
            return false;
        }
        ProjectNotif other = (ProjectNotif) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ProjectNotif[id=" + id + "]";
    }

}
