/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

/**
 *
 * @author yc06x
 */
public class IndicatorChoices {

    private int indicatorId;
    private String label;
    private Float score;
    private Integer mask;

    public void setIndicatorId(int v) {
        this.indicatorId = v;
    }

    public int getIndicatorId() {
        return this.indicatorId;
    }

    public void setLabel(String v) {
        this.label = v;
    }

    public String getLabel() {
        return this.label;
    }

    public void setScore(Float v) {
        this.score = v;
    }

    public Float getScore() {
        return this.score;
    }

    public void setMask(Integer v) {
        this.mask = v;
    }

    public Integer getMask() {
        return this.mask;
    }

}
