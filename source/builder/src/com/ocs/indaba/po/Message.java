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
@Table(name = "message")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
    @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id"),
    @NamedQuery(name = "Message.findByMsgboardId", query = "SELECT m FROM Message m WHERE m.msgboardId = :msgboardId"),
    @NamedQuery(name = "Message.findByPublishable", query = "SELECT m FROM Message m WHERE m.publishable = :publishable"),
    @NamedQuery(name = "Message.findByAuthorUserId", query = "SELECT m FROM Message m WHERE m.authorUserId = :authorUserId"),
    @NamedQuery(name = "Message.findByCreatedTime", query = "SELECT m FROM Message m WHERE m.createdTime = :createdTime"),
    @NamedQuery(name = "Message.findByTitle", query = "SELECT m FROM Message m WHERE m.title = :title"),
    @NamedQuery(name = "Message.findByEnhancerUserId", query = "SELECT m FROM Message m WHERE m.enhancerUserId = :enhancerUserId"),
    @NamedQuery(name = "Message.findByEnhanceTime", query = "SELECT m FROM Message m WHERE m.enhanceTime = :enhanceTime"),
    @NamedQuery(name = "Message.findByEnhanceTitle", query = "SELECT m FROM Message m WHERE m.enhanceTitle = :enhanceTitle"),
    @NamedQuery(name = "Message.findByDeleteTime", query = "SELECT m FROM Message m WHERE m.deleteTime = :deleteTime"),
    @NamedQuery(name = "Message.findByDeleteUserId", query = "SELECT m FROM Message m WHERE m.deleteUserId = :deleteUserId")})
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "msgboard_id")
    private int msgboardId;
    @Column(name = "publishable")
    private Boolean publishable;
    @Basic(optional = false)
    @Column(name = "author_user_id")
    private int authorUserId;
    @Basic(optional = false)
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "body")
    private String body;
    @Column(name = "enhancer_user_id")
    private Integer enhancerUserId;
    @Column(name = "enhance_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enhanceTime;
    @Lob
    @Column(name = "enhance_body")
    private String enhanceBody;
    @Column(name = "enhance_title")
    private String enhanceTitle;
    @Column(name = "delete_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;
    @Column(name = "delete_user_id")
    private Integer deleteUserId;

    public Message() {
    }

    public Message(Integer id) {
        this.id = id;
    }

    public Message(Integer id, int msgboardId, int authorUserId, Date createdTime) {
        this.id = id;
        this.msgboardId = msgboardId;
        this.authorUserId = authorUserId;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMsgboardId() {
        return msgboardId;
    }

    public void setMsgboardId(int msgboardId) {
        this.msgboardId = msgboardId;
    }

    public Boolean getPublishable() {
        return publishable;
    }

    public void setPublishable(Boolean publishable) {
        this.publishable = publishable;
    }

    public int getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(int authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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

    public Integer getEnhancerUserId() {
        return enhancerUserId;
    }

    public void setEnhancerUserId(Integer enhancerUserId) {
        this.enhancerUserId = enhancerUserId;
    }

    public Date getEnhanceTime() {
        return enhanceTime;
    }

    public void setEnhanceTime(Date enhanceTime) {
        this.enhanceTime = enhanceTime;
    }

    public String getEnhanceBody() {
        return enhanceBody;
    }

    public void setEnhanceBody(String enhanceBody) {
        this.enhanceBody = enhanceBody;
    }

    public String getEnhanceTitle() {
        return enhanceTitle;
    }

    public void setEnhanceTitle(String enhanceTitle) {
        this.enhanceTitle = enhanceTitle;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getDeleteUserId() {
        return deleteUserId;
    }

    public void setDeleteUserId(Integer deleteUserId) {
        this.deleteUserId = deleteUserId;
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
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Message[ id=" + id + " ]";
    }
    
}
