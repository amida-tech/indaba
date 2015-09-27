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
@Table(name = "answer_object_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerObjectChoice.findAll", query = "SELECT a FROM AnswerObjectChoice a"),
    @NamedQuery(name = "AnswerObjectChoice.findById", query = "SELECT a FROM AnswerObjectChoice a WHERE a.id = :id"),
    @NamedQuery(name = "AnswerObjectChoice.findByChoices", query = "SELECT a FROM AnswerObjectChoice a WHERE a.choices = :choices")})
public class AnswerObjectChoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "choices")
    private long choices;

    public AnswerObjectChoice() {
    }

    public AnswerObjectChoice(Integer id) {
        this.id = id;
    }

    public AnswerObjectChoice(Integer id, long choices) {
        this.id = id;
        this.choices = choices;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getChoices() {
        return choices;
    }

    public void setChoices(long choices) {
        this.choices = choices;
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
        if (!(object instanceof AnswerObjectChoice)) {
            return false;
        }
        AnswerObjectChoice other = (AnswerObjectChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerObjectChoice[ id=" + id + " ]";
    }
    
}
