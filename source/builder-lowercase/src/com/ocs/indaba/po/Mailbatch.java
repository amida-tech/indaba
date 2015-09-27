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
@Table(name = "mailbatch")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mailbatch.findAll", query = "SELECT m FROM Mailbatch m"),
    @NamedQuery(name = "Mailbatch.findById", query = "SELECT m FROM Mailbatch m WHERE m.id = :id"),
    @NamedQuery(name = "Mailbatch.findByFromEmail", query = "SELECT m FROM Mailbatch m WHERE m.fromEmail = :fromEmail"),
    @NamedQuery(name = "Mailbatch.findByToEmail", query = "SELECT m FROM Mailbatch m WHERE m.toEmail = :toEmail"),
    @NamedQuery(name = "Mailbatch.findByCreateTime", query = "SELECT m FROM Mailbatch m WHERE m.createTime = :createTime"),
    @NamedQuery(name = "Mailbatch.findBySendCount", query = "SELECT m FROM Mailbatch m WHERE m.sendCount = :sendCount"),
    @NamedQuery(name = "Mailbatch.findByLastSendTime", query = "SELECT m FROM Mailbatch m WHERE m.lastSendTime = :lastSendTime"),
    @NamedQuery(name = "Mailbatch.findByIsSent", query = "SELECT m FROM Mailbatch m WHERE m.isSent = :isSent"),
    @NamedQuery(name = "Mailbatch.findByMessageId", query = "SELECT m FROM Mailbatch m WHERE m.messageId = :messageId")})
public class Mailbatch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Column(name = "subject")
    private String subject;
    @Lob
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @Column(name = "from_email")
    private String fromEmail;
    @Basic(optional = false)
    @Column(name = "to_email")
    private String toEmail;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "send_count")
    private Integer sendCount;
    @Column(name = "last_send_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSendTime;
    @Column(name = "is_sent")
    private Boolean isSent;
    @Column(name = "message_id")
    private Integer messageId;

    public Mailbatch() {
    }

    public Mailbatch(Integer id) {
        this.id = id;
    }

    public Mailbatch(Integer id, String fromEmail, String toEmail, Date createTime) {
        this.id = id;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Date getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(Date lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    public Boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
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
        if (!(object instanceof Mailbatch)) {
            return false;
        }
        Mailbatch other = (Mailbatch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Mailbatch[ id=" + id + " ]";
    }
    
}
