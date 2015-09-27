/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.survey.table;

import com.ocs.indaba.dao.AnswerObjectDAO;
import com.ocs.indaba.vo.SurveyAnswerObject;
import java.util.List;

/**
 *
 * @author yc06x
 */
public interface AnswerObjectRetriever {

    public List<SurveyAnswerObject> getAnswerObjects(int anchorObjId, AnswerObjectDAO dao, int surveyQuestionId);

}
