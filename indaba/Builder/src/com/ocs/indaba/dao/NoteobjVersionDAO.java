/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.NoteobjVersion;

/**
 *
 * @author yc06x
 */
public class NoteobjVersionDAO extends SmartDaoMySqlImpl<NoteobjVersion, Integer> {

    private static final String SELECT_VERSION =
            "SELECT * FROM noteobj_version WHERE notedef_id=? AND survey_question_id=? AND content_version_id=?";

    public NoteobjVersion getVersion(int notedefId, int surveyQuestionId, int contentVersionId) {
        return super.findSingle(SELECT_VERSION, notedefId, surveyQuestionId, contentVersionId);
    }

}
