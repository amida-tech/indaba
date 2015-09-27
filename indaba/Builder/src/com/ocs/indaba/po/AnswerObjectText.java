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
@Table(name = "answer_object_text")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerObjectText.findAll", query = "SELECT a FROM AnswerObjectText a"),
    @NamedQuery(name = "AnswerObjectText.findById", query = "SELECT a FROM AnswerObjectText a WHERE a.id = :id")})
public class AnswerObjectText implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "value")
    private String value;

    public AnswerObjectText() {
    }

    public AnswerObjectText(Integer id) {
        this.id = id;
    }

    public AnswerObjectText(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
        if (!(object instanceof AnswerObjectText)) {
            return false;
        }
        AnswerObjectText other = (AnswerObjectText) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerObjectText[ id=" + id + " ]";
    }
    
}
