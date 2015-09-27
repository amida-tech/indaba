/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.SurveyPeerReview;

/**
 *
 * @author yc06x
 */
public class SurveyPeerReviewBasicView extends SurveyPeerReview {

    private int questionId;

    public void setQuestionId(int qstId) {
        this.questionId = qstId;
    }

    public int getQuestionId() {
        return this.questionId;
    }

}
