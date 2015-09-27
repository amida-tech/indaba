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
@Table(name = "userfinder_event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserfinderEvent.findAll", query = "SELECT u FROM UserfinderEvent u"),
    @NamedQuery(name = "UserfinderEvent.findById", query = "SELECT u FROM UserfinderEvent u WHERE u.id = :id"),
    @NamedQuery(name = "UserfinderEvent.findByUserfinderId", query = "SELECT u FROM UserfinderEvent u WHERE u.userfinderId = :userfinderId"),
    @NamedQuery(name = "UserfinderEvent.findByUserId", query = "SELECT u FROM UserfinderEvent u WHERE u.userId = :userId"),
    @NamedQuery(name = "UserfinderEvent.findByCasesId", query = "SELECT u FROM UserfinderEvent u WHERE u.casesId = :casesId"),
    @NamedQuery(name = "UserfinderEvent.findByExeTime", query = "SELECT u FROM UserfinderEvent u WHERE u.exeTime = :exeTime")})
public class UserfinderEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "userfinder_id")
    private int userfinderId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "cases_id")
    private int casesId;
    @Basic(optional = false)
    @Column(name = "exe_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exeTime;

    public UserfinderEvent() {
    }

    public UserfinderEvent(Integer id) {
        this.id = id;
    }

    public UserfinderEvent(Integer id, int userfinderId, int userId, int casesId, Date exeTime) {
        this.id = id;
        this.userfinderId = userfinderId;
        this.userId = userId;
        this.casesId = casesId;
        this.exeTime = exeTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserfinderId() {
        return userfinderId;
    }

    public void setUserfinderId(int userfinderId) {
        this.userfinderId = userfinderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCasesId() {
        return casesId;
    }

    public void setCasesId(int casesId) {
        this.casesId = casesId;
    }

    public Date getExeTime() {
        return exeTime;
    }

    public void setExeTime(Date exeTime) {
        this.exeTime = exeTime;
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
        if (!(object instanceof UserfinderEvent)) {
            return false;
        }
        UserfinderEvent other = (UserfinderEvent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.UserfinderEvent[ id=" + id + " ]";
    }
    
}
