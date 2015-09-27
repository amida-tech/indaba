/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyAnswerVersion;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwenbin
 */
public class SurveyAnswerVersionDAO extends SmartDaoMySqlImpl<SurveyAnswerVersion, Integer> {

    private static final Logger logger = Logger.getLogger(SurveyAnswerVersionDAO.class);
    private static final String SELECT_SURVEY_ANSWER_VERSIONS_BY_CONTENT_VERSION_ID =
            "select * from survey_answer_version where content_version_id = ?";
    private static final String SELECT_SAV_BY_CONTENT_VERSION_ID_AND_SURVEY_QUESTION_ID =
            "select * from survey_answer_version where content_version_id=? and survey_question_id=?";

    public List<SurveyAnswerVersion> getSurveyAnswerVersionsByContentVersionId(int contentVersionId){
        return super.find(SELECT_SURVEY_ANSWER_VERSIONS_BY_CONTENT_VERSION_ID, contentVersionId);
    }

    public SurveyAnswerVersion getSurveyAnswerVersionByContentVersionAndSurveyQuestion(int contentVersionId, int surveyQuestionId){
        return super.findSingle(SELECT_SAV_BY_CONTENT_VERSION_ID_AND_SURVEY_QUESTION_ID, contentVersionId, surveyQuestionId);
    }
}
