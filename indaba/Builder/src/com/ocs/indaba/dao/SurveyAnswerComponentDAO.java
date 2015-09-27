/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyAnswerComponent;

/**
 *
 * @author yc06x
 */
public class SurveyAnswerComponentDAO extends SmartDaoMySqlImpl<SurveyAnswerComponent, Integer> {
    
    private static final String SELECT_BY_ANSWER_AND_COMPONENT_ID =
            "SELECT * FROM survey_answer_component WHERE survey_answer_id=? AND component_indicator_id=?";

    public SurveyAnswerComponent selectAnswerComponent(int surveyAnswerId, int componentIndicatorId) {
        return super.findSingle(SELECT_BY_ANSWER_AND_COMPONENT_ID, surveyAnswerId, componentIndicatorId);
    }


    private static final String REMOVE_BY_ANSWER_AND_COMPONENT_ID =
            "DELETE FROM survey_answer_component WHERE survey_answer_id=? AND component_indicator_id=?";

    public void removeAnswerComponent(int surveyAnswerId, int componentIndicatorId) {
        super.delete(REMOVE_BY_ANSWER_AND_COMPONENT_ID, surveyAnswerId, componentIndicatorId);
    }

}
