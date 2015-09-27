/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

/**
 *
 * @author yc06x
 */
public class RefChoice {
    String label;
    int mask;
    int weight;
    String sname;

    public void setLabel(String v) {
        this.label = v;
    }

    public String getLabel() {
        return this.label;
    }

    public void setSname(String v) {
        this.sname = v;
    }

    public String getSname() {
        return this.sname;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }

    public void setWeight(int v) {
        this.weight = v;
    }

    public int getWeight() {
        return this.weight;
    }
}
