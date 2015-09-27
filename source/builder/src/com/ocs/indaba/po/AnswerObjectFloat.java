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
@Table(name = "answer_object_float")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerObjectFloat.findAll", query = "SELECT a FROM AnswerObjectFloat a"),
    @NamedQuery(name = "AnswerObjectFloat.findById", query = "SELECT a FROM AnswerObjectFloat a WHERE a.id = :id"),
    @NamedQuery(name = "AnswerObjectFloat.findByValue", query = "SELECT a FROM AnswerObjectFloat a WHERE a.value = :value")})
public class AnswerObjectFloat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private Float value;

    public AnswerObjectFloat() {
    }

    public AnswerObjectFloat(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
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
        if (!(object instanceof AnswerObjectFloat)) {
            return false;
        }
        AnswerObjectFloat other = (AnswerObjectFloat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerObjectFloat[ id=" + id + " ]";
    }
    
}
