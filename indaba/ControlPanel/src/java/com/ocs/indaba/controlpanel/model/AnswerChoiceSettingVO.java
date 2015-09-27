/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.po.AtcChoiceIntl;
import com.ocs.util.ValueObject;

/**
 *
 * @author Jeff Jiang
 */
public class AnswerChoiceSettingVO extends ValueObject {

    private int id;
    private int atcChoiceId;
    private String label = null;
    private double value ;
    private boolean useScore = false;
    private String criteria = null;

    public AnswerChoiceSettingVO() {
    }

    public static AnswerChoiceSettingVO initWithAtcChoice(AtcChoice atcChoice) {
        AnswerChoiceSettingVO vo = new AnswerChoiceSettingVO();
        vo.setId(atcChoice.getId());
        vo.setAtcChoiceId(atcChoice.getId());
        vo.setLabel(atcChoice.getLabel());
        vo.setUseScore(atcChoice.getUseScore());
        vo.setValue((double)(atcChoice.getScore()) / 10000.0);  // must divide by 10000
        vo.setCriteria(atcChoice.getCriteria());
        return vo;
    }

    /*
    public static AnswerChoiceSettingVO initWithAtcChoice(AtcChoiceIntl atcChoice) {
        AnswerChoiceSettingVO vo = new AnswerChoiceSettingVO();
        vo.setId(atcChoice.getId());
        vo.setLabel(atcChoice.getLabel());
        vo.setCriteria(atcChoice.getCriteria());
        return vo;
    }
     * 
     */

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = (criteria == null) ? "" : criteria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAtcChoiceId() {
        return atcChoiceId;
    }

    public void setAtcChoiceId(int atcChoiceId) {
        this.atcChoiceId = atcChoiceId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isUseScore() {
        return useScore;
    }

    public void setUseScore(boolean useScore) {
        this.useScore = useScore;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AnswerVO{" + "id=" + id + ", label=" + label + ", value=" + value + ", useScore=" + useScore + ", criteria=" + criteria + '}';
    }
}
