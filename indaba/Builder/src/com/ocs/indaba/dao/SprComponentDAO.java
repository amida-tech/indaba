/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SprComponent;

/**
 *
 * @author yc06x
 */
public class SprComponentDAO extends SmartDaoMySqlImpl<SprComponent, Integer> {

    private static final String SELECT_BY_PR_AND_COMPONENT_ID =
            "SELECT * FROM spr_component WHERE survey_peer_review_id=? AND component_indicator_id=?";

    public SprComponent selectPeerReviewComponent(int surveyPeerReviewId, int componentIndicatorId) {
        return super.findSingle(SELECT_BY_PR_AND_COMPONENT_ID, surveyPeerReviewId, componentIndicatorId);
    }


    private static final String REMOVE_BY_PR_AND_COMPONENT_ID =
            "DELETE FROM spr_component WHERE survey_peer_review_id=? AND component_indicator_id=?";

    public void removePeerReviewComponent(int surveyPeerReviewId, int componentIndicatorId) {
        super.delete(REMOVE_BY_PR_AND_COMPONENT_ID, surveyPeerReviewId, componentIndicatorId);
    }
}
