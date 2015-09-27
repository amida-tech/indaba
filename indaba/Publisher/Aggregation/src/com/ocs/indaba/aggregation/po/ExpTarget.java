/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.po;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jiangjeff
 */
@Entity
@Table(name = "exp_target")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpTarget.findAll", query = "SELECT e FROM ExpTarget e"),
    @NamedQuery(name = "ExpTarget.findById", query = "SELECT e FROM ExpTarget e WHERE e.id = :id"),
    @NamedQuery(name = "ExpTarget.findByExportLogId", query = "SELECT e FROM ExpTarget e WHERE e.exportLogId = :exportLogId"),
    @NamedQuery(name = "ExpTarget.findByTargetId", query = "SELECT e FROM ExpTarget e WHERE e.targetId = :targetId")})
public class ExpTarget implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "export_log_id")
    private int exportLogId;
    @Basic(optional = false)
    @Column(name = "target_id")
    private int targetId;

    public ExpTarget() {
    }

    public ExpTarget(Integer id) {
        this.id = id;
    }

    public ExpTarget(Integer id, int exportLogId, int targetId) {
        this.id = id;
        this.exportLogId = exportLogId;
        this.targetId = targetId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getExportLogId() {
        return exportLogId;
    }

    public void setExportLogId(int exportLogId) {
        this.exportLogId = exportLogId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
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
        if (!(object instanceof ExpTarget)) {
            return false;
        }
        ExpTarget other = (ExpTarget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.ExpTarget[ id=" + id + " ]";
    }
    
}
