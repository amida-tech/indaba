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
@Table(name = "notification_item")
@NamedQueries({
    @NamedQuery(name = "NotificationItem.findAll", query = "SELECT n FROM NotificationItem n"),
    @NamedQuery(name = "NotificationItem.findById", query = "SELECT n FROM NotificationItem n WHERE n.id = :id"),
    @NamedQuery(name = "NotificationItem.findBySubjectText", query = "SELECT n FROM NotificationItem n WHERE n.subjectText = :subjectText"),
    @NamedQuery(name = "NotificationItem.findByLanguageId", query = "SELECT n FROM NotificationItem n WHERE n.languageId = :languageId"),
    @NamedQuery(name = "NotificationItem.findByNotificationTypeId", query = "SELECT n FROM NotificationItem n WHERE n.notificationTypeId = :notificationTypeId"),
    @NamedQuery(name = "NotificationItem.findByName", query = "SELECT n FROM NotificationItem n WHERE n.name = :name")})
public class NotificationItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "subject_text")
    private String subjectText;
    @Lob
    @Column(name = "body_text")
    private String bodyText;
    @Column(name = "language_id")
    private Integer languageId;
    @Column(name = "notification_type_id")
    private Integer notificationTypeId;
    @Column(name = "name")
    private String name;

    public NotificationItem() {
    }

    public NotificationItem(Integer id) {
        this.id = id;
    }

    public NotificationItem(Integer id, String subjectText) {
        this.id = id;
        this.subjectText = subjectText;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(Integer notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof NotificationItem)) {
            return false;
        }
        NotificationItem other = (NotificationItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.NotificationItem[id=" + id + "]";
    }

}
