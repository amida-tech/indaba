/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Tool;
import com.ocs.indaba.service.SurveyCategoryService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.ToolService;
import com.ocs.indaba.service.HorseService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.LoginUser;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class ScorecardNavigationAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ScorecardNavigationAction.class);
    private static final String ATTR_SHOW_IAMDONE_BUTTON = "showIamdoneButton";
    private static final String ATTR_INDICATOR_IN_STR = "indicatorinstr";
    private static final String ATTR_PROBLEMS = "problems";
    private static final String PARAM_DISPLAY_SUBMIT = "displaySubmit";
    private static final String ATTR_SURVEY_TITLE = "surveyTitle";
    private static final String ATTR_CONTENT_HEADER = "contentHeader";
    private static final String ATTR_CONTENT_VERSION = "contentVersion";
    private ToolService toolService;
    private TaskService taskService;
    private HorseService horseService;
    private SurveyCategoryService surveyCategoryService = null;
    private SurveyService surveyService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);

        List<AssignedTask> myAssignedTasks = taskService.getAssignedTasksByUserId(loginUser.getUid(), loginUser.getPrjid());
        if (myAssignedTasks != null) {
            for (AssignedTask task : myAssignedTasks) {
                //task.setCompleted(horseService.getCompletedPercentage(task.getHorseId()));
                /**************************
                int percentage = (int) (task.getPercentage() * 100);
                if (percentage < 0) {
                task.setCompleteDisplay("--");
                } else if (percentage > 95 && percentage < 100) {
                task.setCompleteDisplay("95");
                } else {
                task.setCompleteDisplay(String.valueOf(percentage));
                }
                 *****************************/
                Tool tool = toolService.getToolById(task.getToolId());
                if (tool != null) {
                    task.setToolName(tool.getName());
                    task.setAction(tool.getAction());
                }
            }
        }

        int horseId = StringUtils.str2int(request.getParameter(ATTR_HORSE_ID), Constants.INVALID_INT_ID);
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        int completedAnswerCount = 0;

        String returlParm = request.getParameter(ATTR_RET_URL);
        String returl = "";
        if (returlParm != null) {
            returl = URLDecoder.decode(returlParm, "UTF-8");
            URL url = new URL(returl);
            returl = url.getPath().replaceAll(request.getContextPath() + "/", "");
        }

        boolean showIamdoneButton = true;

        if (assignId == 0) {
            showIamdoneButton = false;
        } else if ("surveyReviewResponse.do".equals(returl) && !surveyService.allSurveyProblemAnswered(horseId)) {
            showIamdoneButton = false;
        } else {
            if ("surveyPeerReview.do".equals(returl)) {
                completedAnswerCount = surveyCategoryService.getCompletedSurveyPeerReviewCountByHorseIdAndReviewerId(horseId, loginUser.getUid());
            } else if ("surveyEdit.do".equals(returl)) {
                completedAnswerCount = surveyCategoryService.getCompletedEditedSurveyAnswerCountByHorseId(horseId);
            } else {
                completedAnswerCount = surveyCategoryService.getCompletedSurveyAnswerCountByHorseId(horseId);
            }

            int questionsCount = surveyCategoryService.getSurveyQuestionCountByHorseId(horseId);
            logger.debug("====> questionsCount=" + questionsCount + ", completedAnswerCount: " + completedAnswerCount);

            //TaskAssignment ta = taskService.getTaskAssignment(assignId);

            if ("surveyDisplay.do".equals(returl)
                    || (("surveyCreate.do".equals(returl) || "surveyEdit.do".equals(returl) || "surveyPeerReview.do".equals(returl))
                    && (questionsCount != completedAnswerCount))) {
                showIamdoneButton = false;
            }
        }

        /*
        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
        if ((assignedTask != null)
                && (assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE
                || assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW
                || assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_PR_REVIEW)) {
            List<SurveyAnswerProblemVO> problems = surveyService.getSurveyAnswerProblems(horseId);
            request.setAttribute(ATTR_PROBLEMS, problems);
            request.setAttribute(PARAM_DISPLAY_SUBMIT, !taskService.reviewResponseAssignmentExist(horseId, "survey review response"));
            
            List<UserDisplay> assignedUsers = horseService.getAssignedUsers(horseId, loginUser.getPrjid(), loginUser.getUid());
            request.setAttribute("assignedusers", assignedUsers);
        }
        */
        
        ContentHeader contentHeader = horseService.getContentHeaderByHorseId(horseId);

        request.setAttribute(ATTR_ACTION, request.getParameter(ATTR_ACTION));
        request.setAttribute(ATTR_HORSE_ID, request.getParameter(ATTR_HORSE_ID));
        request.setAttribute(ATTR_RET_URL, request.getParameter(ATTR_RET_URL));
        request.setAttribute(ATTR_SHOW_IAMDONE_BUTTON, showIamdoneButton);
        request.setAttribute(PARAM_ASSIGNID, request.getParameter(PARAM_ASSIGNID));
        request.setAttribute(ATTR_MY_ASSIGNED_OF_HORSE_LIST, myAssignedTasks);
        request.setAttribute(ATTR_INDICATOR_IN_STR, surveyService.getInstructionsbyHorseId(horseId));
        request.setAttribute(ATTR_SURVEY_TITLE, contentHeader.getTitle());
        request.setAttribute(ATTR_CONTENT_HEADER, contentHeader);
        request.setAttribute(ATTR_CONTENT_VERSION_LIST, surveyService.getAllContentVersions(horseId));
        int contentVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_VERSION_ID));
        request.setAttribute(ATTR_CONTENT_VERSION, surveyService.getContentVersion(contentVersionId));
        request.setAttribute(PARAM_CONTENT_VERSION_ID, contentVersionId);

        return mapping.findForward(FWD_SUCCESS);
    }

    @Autowired
    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setSurveyCategoryService(SurveyCategoryService surveyCategoryService) {
        this.surveyCategoryService = surveyCategoryService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }
}
