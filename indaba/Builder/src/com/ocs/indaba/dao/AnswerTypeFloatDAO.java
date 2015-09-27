/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.AnswerTypeFloat;
import java.util.List;

/**
 *
 * @author seanpcheng
 */
public class AnswerTypeFloatDAO extends SmartDaoMySqlImpl<AnswerTypeFloat, Integer>{

    private static final String SELECT_ATF_OF_SURVEY_CONFIG =
            "SELECT DISTINCT at.* " +
            "FROM answer_type_float at, survey_question sq, survey_indicator si " +
            "WHERE sq.survey_config_id=? AND si.id = sq.survey_indicator_id AND si.answer_type=4 " +
            "AND at.id = si.answer_type_id";

    public List<AnswerTypeFloat> getTypeDefsOfSurveyConfig(int surveyConfigId) {
        return super.find(SELECT_ATF_OF_SURVEY_CONFIG, surveyConfigId);
    }

}
