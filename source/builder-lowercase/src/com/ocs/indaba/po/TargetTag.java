/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.po;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "target_tag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TargetTag.findAll", query = "SELECT t FROM TargetTag t"),
    @NamedQuery(name = "TargetTag.findById", query = "SELECT t FROM TargetTag t WHERE t.id = :id"),
    @NamedQuery(name = "TargetTag.findByTargetId", query = "SELECT t FROM TargetTag t WHERE t.targetId = :targetId"),
    @NamedQuery(name = "TargetTag.findByTtagsId", query = "SELECT t FROM TargetTag t WHERE t.ttagsId = :ttagsId")})
public class TargetTag implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "target_id")
    private int targetId;
    @Basic(optional = false)
    @Column(name = "ttags_id")
    private int ttagsId;

    public TargetTag() {
    }

    public TargetTag(Integer id) {
        this.id = id;
    }

    public TargetTag(Integer id, int targetId, int ttagsId) {
        this.id = id;
        this.targetId = targetId;
        this.ttagsId = ttagsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getTtagsId() {
        return ttagsId;
    }

    public void setTtagsId(int ttagsId) {
        this.ttagsId = ttagsId;
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
        if (!(object instanceof TargetTag)) {
            return false;
        }
        TargetTag other = (TargetTag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.TargetTag[ id=" + id + " ]";
    }
    
}
