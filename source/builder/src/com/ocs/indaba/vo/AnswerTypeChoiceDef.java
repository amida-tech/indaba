/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.AtcChoice;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class AnswerTypeChoiceDef {

    private int answerTypeId;
    private List<AtcChoice> choices;

    public void setAnswerTypeId(int value) {
        this.answerTypeId = value;
    }

    public int getAnswerTypeId() {
        return this.answerTypeId;
    }

    public void setChoices(List<AtcChoice> value) {
        this.choices = value;
    }

    public List<AtcChoice> getChoices() {
        return this.choices;
    }

}
