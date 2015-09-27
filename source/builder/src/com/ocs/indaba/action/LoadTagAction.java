/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.vo.LoginUser;

/**
 *
 * @author flyaway
 */
public class LoadTagAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerAction.class);
    private SurveyService surveyService;
    private HorseService horseService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);
        
        String strHorseId = request.getParameter(PARAM_HORSE_ID);
        int horseId = NumberUtils.toInt(strHorseId, 0);
        String strQuestionId = request.getParameter(PARAM_QUESTION_ID);
        int surveyQuestionId = NumberUtils.toInt(strQuestionId, 0);
        request.setAttribute(ATTR_HORSE_ID, horseId);
        request.setAttribute(ATTR_QUESTION_ID, surveyQuestionId);

        String strAnswerId = request.getParameter(PARAM_ANSWER_ID);
        int answerId;
        if (strAnswerId == null){
            answerId = surveyService.getSurveyAnswerId(surveyQuestionId, horseId, loginUser.getUid());
        }
        else{
            answerId = NumberUtils.toInt(strAnswerId, 0);
        }
        request.setAttribute(ATTR_ANSWER_ID, answerId);

        Integer ContentObjectId = horseService.getContentHeaderByHorseId(horseId).getContentObjectId();
        request.setAttribute("contentobjectid", ContentObjectId);

        return null;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }
}