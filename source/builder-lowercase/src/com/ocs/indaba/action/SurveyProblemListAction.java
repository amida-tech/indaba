/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;

import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.SurveyAnswerProblemVO;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.vo.AssignedTask;

import java.util.List;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Luke
 */
public class SurveyProblemListAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyProblemListAction.class);
    private static final String ATTR_PROBLEM_LIST = "problemList";
    private static final String PARAM_DISPLAY_HAVE_QUESTIONS = "displayHaveQuestions";

    private SurveyService surveyService = null;
    private TaskService taskService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        preprocess(mapping, request, response);
        
        String action = request.getParameter("action");
        int horseId = StringUtils.str2int(request.getParameter(ATTR_HORSE_ID), Constants.INVALID_INT_ID);
        
        if ("addall".equals(action)) {
            surveyService.addAllSurveyAnswersToQuestionList(horseId);
            
            /*
            int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
            AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
            if ((assignedTask != null)
                    && (assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE)) {
                List<SurveyAnswerProblemVO> problems = surveyService.getSurveyAnswerProblems(horseId);
                request.setAttribute("problems", problems);
            }
            
            request.setAttribute(ATTR_HORSEID, horseId);
            request.setAttribute(PARAM_ASSIGNID, assignId);
            request.setAttribute(ATTR_ACTION, getActionPath(request));
            */ 
            return null;        // mapping.findForward("questionlist");
        }
        
        if ("addtagged".equals(action)) {
            String tag = request.getParameter("tag");
            surveyService.addTaggedSurveyAnswersToQuestionList(horseId, tag);
            return null;
        }
        
        if ("remove".equals(action)) {
            int answerId = StringUtils.str2int(request.getParameter("said"), Constants.INVALID_INT_ID);
            SurveyAnswer sa = surveyService.getSurveyAnswerById(answerId);
            sa.setReviewerHasProblem(false);
            surveyService.saveSurveyAnswer(sa);
            return null;
        }
        
        int assignId = StringUtils.str2int(request.getParameter("assignid"), Constants.INVALID_INT_ID);
        int answerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
        int dispAnswerId = StringUtils.str2int(request.getParameter("disp_answerid"), Constants.INVALID_INT_ID);

        SurveyAnswer answer = null;
        if (answerId > 0) {
            answer = surveyService.getSurveyAnswerById(answerId);
            answer.setReviewerHasProblem(true);
            answer.setAuthorResponded(false);
            surveyService.saveSurveyAnswer(answer);
        } else if (answerId < 0) {
            answer = surveyService.getSurveyAnswerById(answerId * -1);
            answer.setReviewerHasProblem(false);
            answer.setAuthorResponded(false);
            surveyService.saveSurveyAnswer(answer);
        }

        List<SurveyAnswerProblemVO> problemList = surveyService.getSurveyAnswerProblems(horseId, super.getLanguageId(request));
        request.setAttribute(ATTR_PROBLEM_LIST, problemList);
        request.setAttribute(ATTR_ACTION, action);
        request.setAttribute(ATTR_HORSE_ID, horseId);
        request.setAttribute(ATTR_ASSIGNID, assignId);
        request.setAttribute(ATTR_ANSWER_ID, answerId);
        request.setAttribute("disp_answerid", dispAnswerId);
        String reviewResponse = request.getParameter("reviewResponse");
        request.setAttribute("reviewResponse", reviewResponse);

        //String returl = request.getParameter("returl");
        String returl = URLEncoder.encode(request.getParameter("returl"), "UTF-8");
        request.setAttribute("returl", returl);

        if (reviewResponse == null || !reviewResponse.equals("TRUE")) {
            SurveyAnswer dispAnswer = surveyService.getSurveyAnswerById(dispAnswerId);
            request.setAttribute(PARAM_DISPLAY_HAVE_QUESTIONS, (dispAnswer == null) || (dispAnswer.getReviewerHasProblem() == false));
        }
        return mapping.findForward(FWD_SUCCESS);
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
