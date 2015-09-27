/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "noteobj_intl")
@NamedQueries({
    @NamedQuery(name = "NoteobjIntl.findAll", query = "SELECT n FROM NoteobjIntl n"),
    @NamedQuery(name = "NoteobjIntl.findById", query = "SELECT n FROM NoteobjIntl n WHERE n.id = :id"),
    @NamedQuery(name = "NoteobjIntl.findByNoteobjId", query = "SELECT n FROM NoteobjIntl n WHERE n.noteobjId = :noteobjId"),
    @NamedQuery(name = "NoteobjIntl.findByLanguageId", query = "SELECT n FROM NoteobjIntl n WHERE n.languageId = :languageId"),
    @NamedQuery(name = "NoteobjIntl.findByCreateUserId", query = "SELECT n FROM NoteobjIntl n WHERE n.createUserId = :createUserId"),
    @NamedQuery(name = "NoteobjIntl.findByCreateTime", query = "SELECT n FROM NoteobjIntl n WHERE n.createTime = :createTime"),
    @NamedQuery(name = "NoteobjIntl.findByLastUpdateUserId", query = "SELECT n FROM NoteobjIntl n WHERE n.lastUpdateUserId = :lastUpdateUserId"),
    @NamedQuery(name = "NoteobjIntl.findByLastUpdateTime", query = "SELECT n FROM NoteobjIntl n WHERE n.lastUpdateTime = :lastUpdateTime")})
public class NoteobjIntl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "noteobj_id")
    private int noteobjId;
    @Basic(optional = false)
    @Column(name = "language_id")
    private int languageId;
    @Basic(optional = false)
    @Lob
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @Column(name = "create_user_id")
    private int createUserId;
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "last_update_user_id")
    private Integer lastUpdateUserId;
    @Column(name = "last_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public NoteobjIntl() {
    }

    public NoteobjIntl(Integer id) {
        this.id = id;
    }

    public NoteobjIntl(Integer id, int noteobjId, int languageId, String note, int createUserId) {
        this.id = id;
        this.noteobjId = noteobjId;
        this.languageId = languageId;
        this.note = note;
        this.createUserId = createUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNoteobjId() {
        return noteobjId;
    }

    public void setNoteobjId(int noteobjId) {
        this.noteobjId = noteobjId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(Integer lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
        if (!(object instanceof NoteobjIntl)) {
            return false;
        }
        NoteobjIntl other = (NoteobjIntl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.NoteobjIntl[id=" + id + "]";
    }

}
