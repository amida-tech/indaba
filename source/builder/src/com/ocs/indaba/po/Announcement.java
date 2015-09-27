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
@Table(name = "announcement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Announcement.findAll", query = "SELECT a FROM Announcement a"),
    @NamedQuery(name = "Announcement.findById", query = "SELECT a FROM Announcement a WHERE a.id = :id"),
    @NamedQuery(name = "Announcement.findByTitle", query = "SELECT a FROM Announcement a WHERE a.title = :title"),
    @NamedQuery(name = "Announcement.findByCreatedByUserId", query = "SELECT a FROM Announcement a WHERE a.createdByUserId = :createdByUserId"),
    @NamedQuery(name = "Announcement.findByCreatedTime", query = "SELECT a FROM Announcement a WHERE a.createdTime = :createdTime"),
    @NamedQuery(name = "Announcement.findByExpiration", query = "SELECT a FROM Announcement a WHERE a.expiration = :expiration"),
    @NamedQuery(name = "Announcement.findByActive", query = "SELECT a FROM Announcement a WHERE a.active = :active")})
public class Announcement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @Column(name = "created_by_user_id")
    private int createdByUserId;
    @Basic(optional = false)
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Basic(optional = false)
    @Column(name = "expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;

    public Announcement() {
    }

    public Announcement(Integer id) {
        this.id = id;
    }

    public Announcement(Integer id, String title, int createdByUserId, Date createdTime, Date expiration, boolean active) {
        this.id = id;
        this.title = title;
        this.createdByUserId = createdByUserId;
        this.createdTime = createdTime;
        this.expiration = expiration;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        if (!(object instanceof Announcement)) {
            return false;
        }
        Announcement other = (Announcement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Announcement[ id=" + id + " ]";
    }
    
}
