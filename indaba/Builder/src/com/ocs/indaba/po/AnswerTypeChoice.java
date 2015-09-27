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
@Table(name = "answer_type_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerTypeChoice.findAll", query = "SELECT a FROM AnswerTypeChoice a"),
    @NamedQuery(name = "AnswerTypeChoice.findById", query = "SELECT a FROM AnswerTypeChoice a WHERE a.id = :id")})
public class AnswerTypeChoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    public AnswerTypeChoice() {
    }

    public AnswerTypeChoice(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof AnswerTypeChoice)) {
            return false;
        }
        AnswerTypeChoice other = (AnswerTypeChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.AnswerTypeChoice[ id=" + id + " ]";
    }
    
}
