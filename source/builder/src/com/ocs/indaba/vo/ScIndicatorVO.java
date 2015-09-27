/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.SurveyIndicator;

/**
 *
 * @author Jeff
 */
public class ScIndicatorVO extends SurveyIndicator{
    private int sconfId;
    private boolean added;

    public int getSconfId() {
        return sconfId;
    }

    public void setSconfId(int sconfId) {
        this.sconfId = sconfId;
    }


    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

}
