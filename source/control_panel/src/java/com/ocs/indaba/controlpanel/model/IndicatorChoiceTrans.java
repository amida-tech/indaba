/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.model;

/**
 *
 * @author yc06x
 */
public class IndicatorChoiceTrans {

    private int atcChoiceIntlId = -1;
    private int atcChoiceId = -1;
    private int langId = -1;
    private String label = null;
    private String criteria = null;

    public int getAtcChoiceId() {
        return this.atcChoiceId;
    }

    public void setAtcChoiceId(int id) {
        this.atcChoiceId = id;
    }

    public int getAtcChoiceIntlId() {
        return this.atcChoiceIntlId;
    }

    public void setAtcChoiceIntlId(int id) {
        this.atcChoiceIntlId = id;
    }

    public int getLanguageId() {
        return this.langId;
    }

    public void setLanguageId(int id) {
        this.langId = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }



}
