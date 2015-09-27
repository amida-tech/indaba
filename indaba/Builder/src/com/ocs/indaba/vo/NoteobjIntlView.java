/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.NoteobjIntl;

/**
 *
 * @author yc06x
 */
public class NoteobjIntlView extends NoteobjIntl {

    private int notedefId;
    private int surveyQuestionId;
    private int horseId;
    private String language;

    public void setNotedefId(int id) {
        this.notedefId = id;
    }

    public int getNotedefId() {
        return this.notedefId;
    }

    public void setSurveyQuestionId(int id) {
        this.surveyQuestionId = id;
    }

    public int getSurveyQuestionId() {
        return this.surveyQuestionId;
    }

    public void setHorseId(int id) {
        this.horseId = id;
    }

    public int getHorseId() {
        return this.horseId;
    }

    public void setLanguage(String lang) {
        this.language = lang;
    }

    public String getLanguage() {
        return this.language;
    }

}
