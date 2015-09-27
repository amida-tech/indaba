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
@Table(name = "msg_reading_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MsgReadingStatus.findAll", query = "SELECT m FROM MsgReadingStatus m"),
    @NamedQuery(name = "MsgReadingStatus.findById", query = "SELECT m FROM MsgReadingStatus m WHERE m.id = :id"),
    @NamedQuery(name = "MsgReadingStatus.findByMessageId", query = "SELECT m FROM MsgReadingStatus m WHERE m.messageId = :messageId"),
    @NamedQuery(name = "MsgReadingStatus.findByUserId", query = "SELECT m FROM MsgReadingStatus m WHERE m.userId = :userId"),
    @NamedQuery(name = "MsgReadingStatus.findByTimestamp", query = "SELECT m FROM MsgReadingStatus m WHERE m.timestamp = :timestamp")})
public class MsgReadingStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "message_id")
    private int messageId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public MsgReadingStatus() {
    }

    public MsgReadingStatus(Integer id) {
        this.id = id;
    }

    public MsgReadingStatus(Integer id, int messageId, int userId, Date timestamp) {
        this.id = id;
        this.messageId = messageId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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
        if (!(object instanceof MsgReadingStatus)) {
            return false;
        }
        MsgReadingStatus other = (MsgReadingStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.MsgReadingStatus[ id=" + id + " ]";
    }
    
}
