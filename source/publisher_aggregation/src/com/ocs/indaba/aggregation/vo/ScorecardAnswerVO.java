/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.po.ScorecardAnswer;

/**
 *
 * @author Jeanbone
 */
public class ScorecardAnswerVO {

    private int orgId;
    private ScorecardAnswer scorecardAnswer;

    /**
     * @return the orgId
     */
    public int getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    /**
     * @return the scorecardAnswer
     */
    public ScorecardAnswer getScorecardAnswer() {
        return scorecardAnswer;
    }

    /**
     * @param scorecardAnswer the scorecardAnswer to set
     */
    public void setScorecardAnswer(ScorecardAnswer scorecardAnswer) {
        this.scorecardAnswer = scorecardAnswer;
    }
}
