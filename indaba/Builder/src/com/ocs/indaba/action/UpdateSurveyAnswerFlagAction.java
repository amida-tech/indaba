/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.vo.LoginUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
/**
 *
 * @author Jeanbone
 */
public class UpdateSurveyAnswerFlagAction extends BaseAction {
    private SurveyService surveyService;

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        LoginUser loginUser = preprocess(mapping, request, response);
        
        int surveyQuestionId = Integer.parseInt(request.getParameter(PARAM_QUESTION_ID));
        int horseId = Integer.parseInt(request.getParameter(PARAM_HORSE_ID));
        int assignId = Integer.parseInt(request.getParameter("assignid"));
        int surveyAnswerId = surveyService.getSurveyAnswerId(surveyQuestionId, horseId, loginUser.getUid());
        surveyService.updateSurveyAnswerFlag(assignId, horseId, surveyAnswerId);

        return null;
    }

    @Autowired
    public void setSurveySerivce(SurveyService surveyService) {
        this.surveyService = surveyService;
    }
}
