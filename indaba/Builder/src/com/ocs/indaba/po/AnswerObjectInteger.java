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
@Table(name = "answer_object_integer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerObjectInteger.findAll", query = "SELECT a FROM AnswerObjectInteger a"),
    @NamedQuery(name = "AnswerObjectInteger.findById", query = "SELECT a FROM AnswerObjectInteger a WHERE a.id = :id"),
    @NamedQuery(name = "AnswerObjectInteger.findByValue", query = "SELECT a FROM AnswerObjectInteger a WHERE a.value = :value")})
public class AnswerObjectInteger implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "value")
    private Integer value;

    public AnswerObjectInteger() {
    }

    public AnswerObjectInteger(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
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
        if (!(object instanceof AnswerObjectInteger)) {
            return false;
        }
        AnswerObjectInteger other = (AnswerObjectInteger) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerObjectInteger[ id=" + id + " ]";
    }
    
}
