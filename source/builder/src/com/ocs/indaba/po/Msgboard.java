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
@Table(name = "msgboard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Msgboard.findAll", query = "SELECT m FROM Msgboard m"),
    @NamedQuery(name = "Msgboard.findById", query = "SELECT m FROM Msgboard m WHERE m.id = :id"),
    @NamedQuery(name = "Msgboard.findByCreateTime", query = "SELECT m FROM Msgboard m WHERE m.createTime = :createTime")})
public class Msgboard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public Msgboard() {
    }

    public Msgboard(Integer id) {
        this.id = id;
    }

    public Msgboard(Integer id, Date createTime) {
        this.id = id;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        if (!(object instanceof Msgboard)) {
            return false;
        }
        Msgboard other = (Msgboard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Msgboard[ id=" + id + " ]";
    }
    
}
