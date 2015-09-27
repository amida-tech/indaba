/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import org.apache.log4j.Logger;

import com.ocs.indaba.po.SurveyPeerReviewVersion;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class SurveyPeerReviewVersionDAO extends SmartDaoMySqlImpl<SurveyPeerReviewVersion, Integer> {

    private static final Logger log = Logger.getLogger(SurveyPeerReviewVersionDAO.class);
    private static final String SELECT_SURVEY_ANSWER_ATTACHMENT_BY_VERSION_ID_AND_REVIEWERID = "SELECT * FROM survey_peer_review_version WHERE survey_answer_version_id=? And reviewer_user_id=? AND submit_time IS NOT NULL";
    private static final String SELECT_SURVEY_ANSWER_ATTACHMENT_BY_VERSION_ID = "SELECT * FROM survey_peer_review_version WHERE survey_answer_version_id=? AND submit_time IS NOT NULL";

    public SurveyPeerReviewVersion selectByVersionId(int saVerId, int reviewerId) {
        return super.findSingle(SELECT_SURVEY_ANSWER_ATTACHMENT_BY_VERSION_ID_AND_REVIEWERID, saVerId, reviewerId);
    }

    public SurveyPeerReviewVersion selectByVersionId(int saVerId) {
        return super.findSingle(SELECT_SURVEY_ANSWER_ATTACHMENT_BY_VERSION_ID, saVerId);
    }

    public List<SurveyPeerReviewVersion> selectAllByVersionId(int saVerId, int reviewerId) {
        return super.find(SELECT_SURVEY_ANSWER_ATTACHMENT_BY_VERSION_ID_AND_REVIEWERID, new Object[]{saVerId, reviewerId});
    }

    public List<SurveyPeerReviewVersion> selectAllByVersionId(int saVerId) {
        return super.find(SELECT_SURVEY_ANSWER_ATTACHMENT_BY_VERSION_ID, saVerId);
    }
}
