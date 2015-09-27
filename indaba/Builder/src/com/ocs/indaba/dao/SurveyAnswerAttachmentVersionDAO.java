/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import org.apache.log4j.Logger;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyAnswerAttachmentVersion;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class SurveyAnswerAttachmentVersionDAO extends SmartDaoMySqlImpl<SurveyAnswerAttachmentVersion, Integer> {

    private static final Logger log = Logger.getLogger(SurveyAnswerAttachmentVersionDAO.class);
    private static final String SELECT_SURVEY_ANSWER_ATTACHMENT_BY_VERSION_ID = "SELECT * FROM survey_answer_attachment_version WHERE survey_answer_version_id=?";

    public List<SurveyAnswerAttachmentVersion> selectByVersionId(int versionId) {
        return super.find(SELECT_SURVEY_ANSWER_ATTACHMENT_BY_VERSION_ID, versionId);
    }
}
