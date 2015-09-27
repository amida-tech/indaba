/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Noteobj;

/**
 *
 * @author yc06x
 */
public class NoteobjDAO extends SmartDaoMySqlImpl<Noteobj, Integer> {

    private static final String SELECT_NOTEOBJ =
            "SELECT * FROM noteobj WHERE notedef_id=? AND horse_id=? AND survey_question_id=?";

    public Noteobj getOrCreate(int notedefId, int horseId, int surveyQuestionId) {
        Noteobj obj = super.findSingle(SELECT_NOTEOBJ, notedefId, horseId, surveyQuestionId);

        if (obj != null) return obj;

        // create a new obj
        obj = new Noteobj();
        obj.setHorseId(horseId);
        obj.setNotedefId(notedefId);
        obj.setSurveyQuestionId(surveyQuestionId);

        return super.create(obj);
    }

}
