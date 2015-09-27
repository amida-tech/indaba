/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.po.SurveyIndicator;

/**
 *
 * @author Gerry
 */
public class PubIndicatorVO {
    private int worksetId;
    private SurveyIndicator indicator;
    private String itags;
    private int wsIndicatorId;

    public PubIndicatorVO(){
        indicator = new SurveyIndicator();            
    }

    public SurveyIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(SurveyIndicator indicator) {
        this.indicator = indicator;
    }

    public String getItags() {
        return itags;
    }

    public void setItags(String itags) {
        this.itags = itags;
    }

    public int getWorksetId() {
        return worksetId;
    }

    public void setWorksetId(int worksetId) {
        this.worksetId = worksetId;
    }

    public int getWsIndicatorId() {
        return wsIndicatorId;
    }

    public void setWsIndicatorId(int wsIndicatorId) {
        this.wsIndicatorId = wsIndicatorId;
    }
}
