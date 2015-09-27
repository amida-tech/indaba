/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "content_payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContentPayment.findAll", query = "SELECT c FROM ContentPayment c"),
    @NamedQuery(name = "ContentPayment.findById", query = "SELECT c FROM ContentPayment c WHERE c.id = :id"),
    @NamedQuery(name = "ContentPayment.findByContentHeaderId", query = "SELECT c FROM ContentPayment c WHERE c.contentHeaderId = :contentHeaderId"),
    @NamedQuery(name = "ContentPayment.findByPaidByUserId", query = "SELECT c FROM ContentPayment c WHERE c.paidByUserId = :paidByUserId"),
    @NamedQuery(name = "ContentPayment.findByAmount", query = "SELECT c FROM ContentPayment c WHERE c.amount = :amount"),
    @NamedQuery(name = "ContentPayment.findByTime", query = "SELECT c FROM ContentPayment c WHERE c.time = :time"),
    @NamedQuery(name = "ContentPayment.findByPayees", query = "SELECT c FROM ContentPayment c WHERE c.payees = :payees"),
    @NamedQuery(name = "ContentPayment.findByTaskAssignmentId", query = "SELECT c FROM ContentPayment c WHERE c.taskAssignmentId = :taskAssignmentId")})
public class ContentPayment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "content_header_id")
    private int contentHeaderId;
    @Basic(optional = false)
    @Column(name = "paid_by_user_id")
    private int paidByUserId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic(optional = false)
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Column(name = "payees")
    private String payees;
    @Lob
    @Column(name = "note")
    private String note;
    @Column(name = "task_assignment_id")
    private Integer taskAssignmentId;

    public ContentPayment() {
    }

    public ContentPayment(Integer id) {
        this.id = id;
    }

    public ContentPayment(Integer id, int contentHeaderId, int paidByUserId, BigDecimal amount, Date time) {
        this.id = id;
        this.contentHeaderId = contentHeaderId;
        this.paidByUserId = paidByUserId;
        this.amount = amount;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getContentHeaderId() {
        return contentHeaderId;
    }

    public void setContentHeaderId(int contentHeaderId) {
        this.contentHeaderId = contentHeaderId;
    }

    public int getPaidByUserId() {
        return paidByUserId;
    }

    public void setPaidByUserId(int paidByUserId) {
        this.paidByUserId = paidByUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPayees() {
        return payees;
    }

    public void setPayees(String payees) {
        this.payees = payees;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getTaskAssignmentId() {
        return taskAssignmentId;
    }

    public void setTaskAssignmentId(Integer taskAssignmentId) {
        this.taskAssignmentId = taskAssignmentId;
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
        if (!(object instanceof ContentPayment)) {
            return false;
        }
        ContentPayment other = (ContentPayment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.ContentPayment[ id=" + id + " ]";
    }
    
}
