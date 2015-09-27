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
@Table(name = "groupobj")
@NamedQueries({
    @NamedQuery(name = "Groupobj.findAll", query = "SELECT g FROM Groupobj g"),
    @NamedQuery(name = "Groupobj.findById", query = "SELECT g FROM Groupobj g WHERE g.id = :id"),
    @NamedQuery(name = "Groupobj.findByGroupdefId", query = "SELECT g FROM Groupobj g WHERE g.groupdefId = :groupdefId"),
    @NamedQuery(name = "Groupobj.findByHorseId", query = "SELECT g FROM Groupobj g WHERE g.horseId = :horseId"),
    @NamedQuery(name = "Groupobj.findBySurveyQuestionId", query = "SELECT g FROM Groupobj g WHERE g.surveyQuestionId = :surveyQuestionId")})
public class Groupobj implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "groupdef_id")
    private int groupdefId;
    @Basic(optional = false)
    @Column(name = "horse_id")
    private int horseId;
    @Basic(optional = false)
    @Column(name = "survey_question_id")
    private int surveyQuestionId;

    public Groupobj() {
    }

    public Groupobj(Integer id) {
        this.id = id;
    }

    public Groupobj(Integer id, int groupdefId, int horseId, int surveyQuestionId) {
        this.id = id;
        this.groupdefId = groupdefId;
        this.horseId = horseId;
        this.surveyQuestionId = surveyQuestionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGroupdefId() {
        return groupdefId;
    }

    public void setGroupdefId(int groupdefId) {
        this.groupdefId = groupdefId;
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
        if (!(object instanceof Groupobj)) {
            return false;
        }
        Groupobj other = (Groupobj) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Groupobj[id=" + id + "]";
    }

}
