/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

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

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "noteobj")
@NamedQueries({
    @NamedQuery(name = "Noteobj.findAll", query = "SELECT n FROM Noteobj n"),
    @NamedQuery(name = "Noteobj.findById", query = "SELECT n FROM Noteobj n WHERE n.id = :id"),
    @NamedQuery(name = "Noteobj.findByNotedefId", query = "SELECT n FROM Noteobj n WHERE n.notedefId = :notedefId"),
    @NamedQuery(name = "Noteobj.findByHorseId", query = "SELECT n FROM Noteobj n WHERE n.horseId = :horseId"),
    @NamedQuery(name = "Noteobj.findBySurveyQuestionId", query = "SELECT n FROM Noteobj n WHERE n.surveyQuestionId = :surveyQuestionId")})
public class Noteobj implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "notedef_id")
    private int notedefId;
    @Basic(optional = false)
    @Column(name = "horse_id")
    private int horseId;
    @Basic(optional = false)
    @Column(name = "survey_question_id")
    private int surveyQuestionId;

    public Noteobj() {
    }

    public Noteobj(Integer id) {
        this.id = id;
    }

    public Noteobj(Integer id, int notedefId, int horseId, int surveyQuestionId) {
        this.id = id;
        this.notedefId = notedefId;
        this.horseId = horseId;
        this.surveyQuestionId = surveyQuestionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNotedefId() {
        return notedefId;
    }

    public void setNotedefId(int notedefId) {
        this.notedefId = notedefId;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getSurveyQuestionId() {
        return surveyQuestionId;
    }

    public void setSurveyQuestionId(int surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
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
        if (!(object instanceof Noteobj)) {
            return false;
        }
        Noteobj other = (Noteobj) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Noteobj[id=" + id + "]";
    }

}
