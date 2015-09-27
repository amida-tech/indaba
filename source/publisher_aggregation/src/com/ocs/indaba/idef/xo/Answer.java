/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

import com.ocs.indaba.idef.common.Constants;

/**
 *
 * @author yc06x
 */
public class Answer {

    private short type;
    private int valChoices;
    private int valInt;
    private double valFloat;
    private String valText;

    private int dboId;

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }

    public void setSingleChoiceValue(int mask) {
        this.type = Constants.INDICATOR_TYPE_SINGLE_CHOICE;
        this.valChoices = mask;
    }

    public void setMultiChoiceValue(int mask) {
        this.type = Constants.INDICATOR_TYPE_MULTI_CHOICE;
        this.valChoices = mask;
    }

    public void setIntValue(int v) {
        this.type = Constants.INDICATOR_TYPE_INTEGER;
        this.valInt = v;
    }

    public void setFloatValue(double v) {
        this.type = Constants.INDICATOR_TYPE_FLOAT;
        this.valFloat = v;
    }

    public void setTextValue(String v) {
        this.type = Constants.INDICATOR_TYPE_TEXT;
        this.valText = v;
    }

    public short getType() {
        return this.type;
    }

    public int getChoiceValue() {
        return this.valChoices;
    }

    public int getIntValue() {
        return this.valInt;
    }

    public double getFloatValue() {
        return this.valFloat;
    }

    public String getTextValue() {
        return this.valText;
    }

}
