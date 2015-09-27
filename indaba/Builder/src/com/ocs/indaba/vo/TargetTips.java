/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.Target;

/**
 *
 * @author sjf
 */
public class TargetTips extends Target {

    private String tip;

    public TargetTips(Target target) {
        if (target != null)
            this.setName(target.getName());
    }

    /**
     * @return the tip
     */
    public String getTip() {
        return tip;
    }

    /**
     * @param aTip the tip to set
     */
    public void setTip(String aTip) {
        tip = aTip;
    }
}
