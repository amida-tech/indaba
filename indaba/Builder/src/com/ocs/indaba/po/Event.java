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
@Table(name = "event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByEventLogId", query = "SELECT e FROM Event e WHERE e.eventLogId = :eventLogId"),
    @NamedQuery(name = "Event.findByEventType", query = "SELECT e FROM Event e WHERE e.eventType = :eventType"),
    @NamedQuery(name = "Event.findByUserId", query = "SELECT e FROM Event e WHERE e.userId = :userId"),
    @NamedQuery(name = "Event.findByTimestamp", query = "SELECT e FROM Event e WHERE e.timestamp = :timestamp")})
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "event_log_id")
    private int eventLogId;
    @Basic(optional = false)
    @Column(name = "event_type")
    private short eventType;
    @Lob
    @Column(name = "event_data")
    private String eventData;
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Integer id, int eventLogId, short eventType, Date timestamp) {
        this.id = id;
        this.eventLogId = eventLogId;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEventLogId() {
        return eventLogId;
    }

    public void setEventLogId(int eventLogId) {
        this.eventLogId = eventLogId;
    }

    public short getEventType() {
        return eventType;
    }

    public void setEventType(short eventType) {
        this.eventType = eventType;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Event[ id=" + id + " ]";
    }
    
}
