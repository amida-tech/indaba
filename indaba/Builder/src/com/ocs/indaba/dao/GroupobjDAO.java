/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Groupobj;

/**
 *
 * @author yc06x
 */
public class GroupobjDAO extends SmartDaoMySqlImpl<Groupobj, Integer> {

    private static final String SELECT_GROUPOBJ =
            "SELECT * FROM groupobj WHERE groupdef_id=? AND horse_id=? AND survey_question_id=?";

    public Groupobj getOrCreate(int groupdefId, int horseId, int surveyQuestionId) {
        Groupobj obj = super.findSingle(SELECT_GROUPOBJ, groupdefId, horseId, surveyQuestionId);

        if (obj != null) return obj;

        // create a new obj
        obj = new Groupobj();
        obj.setHorseId(horseId);
        obj.setGroupdefId(groupdefId);
        obj.setSurveyQuestionId(surveyQuestionId);

        return super.create(obj);
    }

}
