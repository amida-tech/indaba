/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

//import com.ocs.indaba.service.ReferenceService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.vo.SurveyAnswerSubmitView;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.SurveyCategoryService;
import com.ocs.indaba.vo.LoginUser;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author luwb
 */
public class SurveyAnswerSubmitAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerSubmitAction.class);
    private SurveyService surveyService;
    private SurveyCategoryService surveyCategoryService;
    //private ReferenceService referenceService;
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);
        

        String selection = request.getParameter("selection");
        String source = request.getParameter("source");
        String sourceDesc = request.getParameter("sourceDesc");
        logger.debug("================SOURCE DESC: " + sourceDesc + "==========");
        String comments = request.getParameter("comments");
        String strSurveyAnswerId = request.getParameter("surveyAnswerId");
        if (sourceDesc == null || sourceDesc.length() == 0) {
            logger.error("Bad parameter from client: null sourceDesc!");
            Map params = request.getParameterMap();
            Set<Entry> entries = params.entrySet();
            for (Entry e : entries) {
                logger.error(e.getKey() + ": " + e.getValue());
            }
            JsonObject obj = new JsonObject();
            obj.addProperty("fail", true);
            obj.addProperty("msg", getMessage(request, Messages.KEY_COMMON_ALERT_SURVEYANSWER_FAIL));
            try {
            	writeMsgUTF8(response, new Gson().toJson(obj));
            } catch (IOException ex) {
                logger.error("write to client error", ex);
            }
            return null;
        }

        int surveyAnswerId = NumberUtils.toInt(strSurveyAnswerId, 0);
        int assignId = NumberUtils.toInt(request.getParameter("assignid"), 0);
        int horseId = NumberUtils.toInt(request.getParameter(PARAM_HORSE_ID), 0);
        int type = NumberUtils.toInt(request.getParameter("type"), 0);
        String action = request.getParameter("action");
   
        String responseMsg = "";
        SurveyAnswerSubmitView view;

        try {
            view = surveyService.submitSurveyAnswer(surveyAnswerId, selection, source, sourceDesc, comments, loginUser.getUid(), assignId, horseId, type, loginUser.getLanguageId());
        } catch (Exception ex) {
            logger.error("Failed to submit survey answer.", ex);
            Map params = request.getParameterMap();
            Set<Entry> entries = params.entrySet();
            for (Entry e : entries) {
                logger.error(e.getKey() + ": " + e.getValue());
            }
            return null;
        }

        if(view.isSucceed()) {
            responseMsg = getMessage(request, Messages.KEY_COMMON_ALERT_SURVEYANSWER_SUCCESS);
        } else {
            responseMsg = view.getErrorMsg();
        }
        if(action.equalsIgnoreCase("surveyEdit.do")){
            String prJson = request.getParameter("prJson");
            if(prJson != null)
                surveyService.updateSumittedPeerReviews(prJson);
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("fail", false);
        if (action.equalsIgnoreCase("surveyReviewResponse.do")) {
            obj.addProperty("done", surveyService.allSurveyProblemAnswered(horseId));
        } else {
            int completedAnswerCount = 0;
            if (action.equalsIgnoreCase("surveyEdit.do"))
                completedAnswerCount = surveyCategoryService.getCompletedEditedSurveyAnswerCountByHorseId(horseId);
            else
                completedAnswerCount = surveyCategoryService.getCompletedSurveyAnswerCountByHorseId(horseId);
            int questionsCount = surveyCategoryService.getSurveyQuestionCountByHorseId(horseId);
            if(completedAnswerCount != questionsCount)
                obj.addProperty("done", false);
            else
                obj.addProperty("done", true);
        }
        obj.addProperty("msg", responseMsg);
        try {
        	writeMsgUTF8(response, new Gson().toJson(obj));
        } catch (IOException ex) {
            logger.error("bad parameters from client", ex);
        }
        return null;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setSurveyCategoryService(SurveyCategoryService surveyCategoryService) {
        this.surveyCategoryService = surveyCategoryService;
    }
}
