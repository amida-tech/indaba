package com.ocs.indaba.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.service.SurveyCategoryService;
import com.ocs.indaba.vo.SurveyQuestionUrlView;
import java.net.URLEncoder;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.vo.ToolIntl;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.SurveyAnswerService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.ToolService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.indaba.util.DateUtils;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerView;
import com.ocs.indaba.vo.UserDisplay;
import java.util.Date;
import org.apache.log4j.Logger;
import java.util.List;

public class SurveyAnswerPRReviewAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerPRReviewAction.class);
    private SurveyService surveyService;
    private SurveyCategoryService surveyCategoryService;
    private SurveyAnswerService surveyAnswerService;
    private TaskService taskService;
    private HorseService horseService;
    private ToolService toolService;
    private ViewPermissionService viewPermissionService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);
        setRequestUrl(request);

        //session.setAttribute(ATTR_ACTIVE, TAB_YOURCONTENT);

        // call serveyService functions
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
        int surveyQuestionId = 0;
        String strSurveyQuestionId = request.getParameter(PARAM_QUESTION_ID);
        surveyQuestionId = NumberUtils.toInt(strSurveyQuestionId, 0);
        if (surveyQuestionId <= 0) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "questionid"));
            return mapping.findForward(FWD_ERROR);
        }
        
        if (surveyAnswerId == Constants.INVALID_INT_ID) {
            surveyAnswerId = surveyService.getSurveyAnswerId(surveyQuestionId, horseId, loginUser.getUid());
            if (surveyAnswerId <= 0) {
                request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "answerid"));
                return mapping.findForward(FWD_ERROR);
            }
        }

        //int completedAnswerCount = surveyCategoryService.getCompletedSurveyPeerReviewCountByHorseIdAndReviewerId(horseId, loginUser.getUid());
        //int questionsCount = surveyCategoryService.getSurveyQuestionCountByHorseId(horseId);
        request.setAttribute("showIamDone", true/*(completedAnswerCount == questionsCount)*/);

        SurveyAnswerView view = surveyService.getSurveyAnswerView(surveyAnswerId, loginUser.getLanguageId());
        if (view == null) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_DATA_NOTEXISTED, "SurveyAnswer", surveyAnswerId));
            return mapping.findForward(FWD_ERROR);
        }
        int initialFlagGroupId = StringUtils.str2int(request.getParameter(PARAM_INITIAL_FLAG_GROUP_ID), 0);
        view.setInitialFlagGroupId(initialFlagGroupId);
        request.setAttribute("view", view);

        request.setAttribute("attachments", surveyAnswerService.getSurveyAnswerAttachments(view.getSurveyAnswerId()));

        SurveyQuestionUrlView urlView = surveyCategoryService.getSurveyQuestionUrlView(surveyAnswerId, horseId);
        String retUrl = request.getParameter("returl");
        if (retUrl != null) {
            urlView.setReturnUrl(URLEncoder.encode(retUrl, "UTF-8"));
        }
        request.setAttribute("urlView", urlView);
        request.setAttribute("tipinfo", view.getTip());
        request.setAttribute("tipDisplayMethod", view.getTipDisplayMethod());

        List<Target> targetList = trgtService.getOtherTargetsByHorseId(horseId);
        request.setAttribute("targets", targetList);
        request.setAttribute("horseid", horseId);
        request.setAttribute(PARAM_QUESTION_ID, surveyQuestionId);

        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
        if (assignedTask != null) {
            ToolIntl toolIntl = toolService.getToolIntl(assignedTask.getToolId(), loginUser.getLanguageId());
            request.setAttribute("toolIntl", toolIntl);
        }
        setAttributesForSendQuestions(request, horseId, loginUser);

        return mapping.findForward(FWD_SUCCESS);
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
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    @Autowired
    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }
}
