/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.AnswerTypeText;
import java.util.List;

/**
 *
 * @author seanpcheng
 */
public class AnswerTypeTextDAO extends SmartDaoMySqlImpl<AnswerTypeText, Integer>{

    private static final String SELECT_ATT_OF_SURVEY_CONFIG =
            "SELECT DISTINCT at.* " +
            "FROM answer_type_text at, survey_question sq, survey_indicator si " +
            "WHERE sq.survey_config_id=? AND si.id = sq.survey_indicator_id AND si.answer_type=5 " +
            "AND at.id = si.answer_type_id";

    public List<AnswerTypeText> getTypeDefsOfSurveyConfig(int surveyConfigId) {
        return super.find(SELECT_ATT_OF_SURVEY_CONFIG, surveyConfigId);
    }
}
