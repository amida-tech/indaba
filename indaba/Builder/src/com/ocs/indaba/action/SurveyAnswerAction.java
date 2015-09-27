/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import static com.ocs.indaba.action.BaseAction.ATTR_HORSE_ID;
import static com.ocs.indaba.action.BaseAction.ATTR_QUESTION_ID;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.service.CommPanelService;
import com.ocs.indaba.vo.ToolIntl;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.SurveyAnswerService;
import com.ocs.indaba.service.SurveyCategoryService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.ToolService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.indaba.util.DateUtils;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerProblemVO;
import com.ocs.indaba.vo.SurveyAnswerView;
import com.ocs.indaba.vo.SurveyQuestionUrlView;
import com.ocs.indaba.vo.UserDisplay;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class SurveyAnswerAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerAction.class);
    private static final String FWD_SURVEY_QUESTION = "success";
    private static final String ATTR_PROBLEMS = "problemList";
    private SurveyService surveyService;
    private ToolService toolService;
    private SurveyAnswerService surveyAnswerService;
    private SurveyCategoryService surveyCategoryService;
    private TaskService taskService;
    private HorseService horseService;
    private ViewPermissionService viewPermissionService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);
        setRequestUrl(request);

        // call serveyService functions
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), 0);
        request.setAttribute(ATTR_HORSE_ID, horseId);
        
        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), 0);
        request.setAttribute(ATTR_ANSWER_ID, surveyAnswerId);
        
        int surveyQuestionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID), 0);
        request.setAttribute(ATTR_QUESTION_ID, surveyQuestionId);
        
        if (horseId <= 0) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }
        if (surveyAnswerId <= 0) {
            if (surveyQuestionId <= 0) {
                request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "answerid' or 'questionid"));
                return mapping.findForward(FWD_ERROR);
            }
            surveyAnswerId = surveyService.getSurveyAnswerId(surveyQuestionId, horseId, loginUser.getUid());
            if (surveyAnswerId <= 0) {
                request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "answerid"));
                return mapping.findForward(FWD_ERROR);
            }
        }
        SurveyAnswerView view = surveyService.getSurveyAnswerView(surveyAnswerId, loginUser.getLanguageId());
        if (view == null) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_DATA_NOTEXISTED, "SurveyAnswer", surveyAnswerId));
            return mapping.findForward(FWD_ERROR);
        }

        String action = "";
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }
        //logger.debug("-----ACTION---->"+action+"<------");
        boolean showIamdone = true;
        if (action.equalsIgnoreCase("surveyCreate.do")) {
            int completedAnswerCount = surveyCategoryService.getCompletedSurveyAnswerCountByHorseId(horseId);
            int questionsCount = surveyCategoryService.getSurveyQuestionCountByHorseId(horseId);
            if (completedAnswerCount != questionsCount) {
                showIamdone = false;
            }
            request.setAttribute("type", Constants.TASK_TYPE_SURVEY_CREATE);
            request.setAttribute("showStaff", true);
        } else if (action.equalsIgnoreCase("surveyEdit.do")) {
            int completedAnswerCount = surveyCategoryService.getCompletedEditedSurveyAnswerCountByHorseId(horseId);
            int questionsCount = surveyCategoryService.getSurveyQuestionCountByHorseId(horseId);
            if (completedAnswerCount != questionsCount) {
                showIamdone = false;
            }
            request.setAttribute("type", Constants.TASK_TYPE_SURVEY_EDIT);
            request.setAttribute("showStaff", true);
            if (surveyService.hasSubmittedPeerReviews(surveyAnswerId)) {
                request.setAttribute("showPeer", true);
            }
            request.setAttribute("showOriginal", true);
        } else if (action.equalsIgnoreCase("surveyOverallReview.do")) {
            request.setAttribute("type", Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW);
            request.setAttribute("showStaff", true);
            request.setAttribute("showPeer", true);
            request.setAttribute("showOriginal", true);
            setAttributesForSendQuestions(request, horseId, loginUser);
        } else if (action.equalsIgnoreCase("surveyReviewResponse.do")) {
            request.setAttribute("type", Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE);
            request.setAttribute("showOriginal", true);
            request.setAttribute("showPeer", true);
            showIamdone = surveyService.allSurveyProblemAnswered(horseId);
            setAttributesForSendQuestions(request, horseId, loginUser);
        } else {
            request.setAttribute("type", SurveyService.SURVEY_UNKNOWN);
        }

        request.setAttribute("showIamDone", showIamdone);
        List<Target> targetList = trgtService.getOtherTargetsByHorseId(horseId);
        SurveyQuestionUrlView urlView = surveyCategoryService.getSurveyQuestionUrlView(surveyAnswerId, horseId);
        String retUrl = request.getParameter("returl");
        if (retUrl != null) {
            urlView.setReturnUrl(URLEncoder.encode(retUrl, "UTF-8"));
        }

        int initialFlagGroupId = StringUtils.str2int(request.getParameter(PARAM_INITIAL_FLAG_GROUP_ID), 0);
        view.setInitialFlagGroupId(initialFlagGroupId);
        
        // survey answer attachment list
        request.setAttribute("attachments", surveyAnswerService.getSurveyAnswerAttachments(view.getSurveyAnswerId()));
        request.setAttribute("urlView", urlView);
        request.setAttribute("targets", targetList);
        request.setAttribute("surveyAnswer", view);
        request.setAttribute("action", "surveyAnswer.do");
        request.setAttribute("from", action);

        //String strSurveyQuestionId = request.getParameter(PARAM_QUESTION_ID);
        //int surveyQuestionId = NumberUtils.toInt(strSurveyQuestionId, 0);
        request.setAttribute("tipinfo", view.getTip());
        request.setAttribute("tipDisplayMethod", view.getTipDisplayMethod());

        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
        if ((assignedTask != null)
                && (assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE)) {
            List<SurveyAnswerProblemVO> problems = surveyService.getSurveyAnswerProblems(horseId, super.getLanguageId(request));
            request.setAttribute(ATTR_PROBLEMS, problems);
            request.setAttribute("reviewResponse", true);
            request.setAttribute("contents", assignedTask.getContents());
        } else {
            request.setAttribute("contents", "ABCDEFG");
        }
        if (assignedTask != null) {
            ToolIntl toolIntl = toolService.getToolIntl(assignedTask.getToolId(), loginUser.getLanguageId());
            request.setAttribute("toolIntl", toolIntl);
        }
        request.setAttribute(PARAM_ASSIGNID, assignId);

        return mapping.findForward(FWD_SURVEY_QUESTION);
    }

    private void setAttributesForSendQuestions(HttpServletRequest request, int horseId, LoginUser loginUser) {
        request.setAttribute(ATTR_TARGET, trgtService.getTargetByHorseId(horseId));
        List<UserDisplay> assignedUsers = horseService.getAssignedUsers(horseId, loginUser.getPrjid(), loginUser.getUid());
        request.setAttribute("assignedusers", assignedUsers);
        TaskAssignment srrTa = taskService.findExistingReviewResponseAssignment(horseId, "survey review response");
        if (srrTa != null) {
            UserDisplay userDisplay = viewPermissionService.getUserDisplayOfProject(loginUser.getPrjid(),
                    Constants.DEFAULT_VIEW_MATRIX_ID, loginUser.getUid(), srrTa.getAssignedUserId());
            request.setAttribute("auserid", userDisplay.getUserId());

            String contents = "";
            String data = srrTa.getData();
            if (data != null) {
                int l = data.indexOf(' ');
                if (l != -1) {
                    contents = data.substring(l + 1);
                }
            }
            request.setAttribute("contents", contents);

            if (srrTa.getDueTime() != null) {
                request.setAttribute("duedate", srrTa.getDueTime().toString().substring(0, 10));
            }
        } else {
            ContentHeader ch = horseService.getContentHeaderByHorseId(horseId);
            request.setAttribute("auserid", ch.getAuthorUserId());
            int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
            TaskAssignment ta = taskService.getTaskAssignment(assignId);
            if (ta != null) {
                Date dueDate = ta.getDueTime();
                if (dueDate == null || dueDate.before(new Date())) {
                    dueDate = DateUtils.nextIntervalDays(new Date(), 2);
                }
                request.setAttribute("duedate", DateUtils.date2Str(dueDate).substring(0, 10));
            }
        }
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
    public void setSurveyAnswerService(SurveyAnswerService surveyAnswerService) {
        this.surveyAnswerService = surveyAnswerService;
    }

    @Autowired
    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }
}
