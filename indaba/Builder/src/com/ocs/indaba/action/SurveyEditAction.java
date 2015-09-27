/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TargetService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.util.StringUtils;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerProblemVO;
import com.ocs.indaba.vo.UserDisplay;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;

/**
 *
 * @author Jeff
 */
public class SurveyEditAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyEditAction.class);
    private static final String ATTR_PROBLEMS = "problems";
    private static final String PARAM_DISPLAY_SUBMIT = "displaySubmit";
    private TaskService taskService = null;
    private HorseService horseService;
    private SurveyService surveyService = null;
    private ViewPermissionService viewPermissionService = (ViewPermissionService) SpringContextUtil.getBean("viewPermissionService");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        //preprocess(mapping, request, response, true, true);
        LoginUser loginUser = preprocess(mapping, request, response, true, true);

        ActionForward actionFwd = super.checkAssignmentStatus(mapping, request);

        if (actionFwd != null) {
            return actionFwd;
        }

        int horseId = StringUtils.str2int(request.getParameter(ATTR_HORSE_ID), Constants.INVALID_INT_ID);
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        request.setAttribute(ATTR_HORSE_ID, horseId);
        request.setAttribute(PARAM_ASSIGNID, assignId);
        //session.setAttribute(ATTR_ACTION, Constants.SURVEY_ACTION_EDIT);
        request.setAttribute(ATTR_ACTION, getActionPath(request));
        //request.setAttribute(ATTR_ACTION, actionPath);
        request.setAttribute("target", trgtService.getTargetByHorseId(horseId));
        TaskAssignment ta = taskService.getTaskAssignment(assignId);
        if (ta != null && ta.getStatus() < Constants.TASK_STATUS_STARTED) {
            taskService.updateTaskAssignmentStatus(assignId, Constants.TASK_STATUS_STARTED);
        }

        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
        if ((assignedTask != null)
                && (assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE
                || assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW
                || assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_PR_REVIEW)) {
            List<SurveyAnswerProblemVO> problems = surveyService.getSurveyAnswerProblems(horseId, super.getLanguageId(request));
            request.setAttribute(ATTR_PROBLEMS, problems);
            request.setAttribute(PARAM_DISPLAY_SUBMIT, !taskService.reviewResponseAssignmentExist(horseId, "survey review response"));

            List<UserDisplay> assignedUsers = horseService.getAssignedUsers(horseId, loginUser.getPrjid(), loginUser.getUid());
            request.setAttribute("assignedusers", assignedUsers);

            TaskAssignment srrTa = taskService.findExistingReviewResponseAssignment(horseId, "survey review response");
            if (srrTa != null) {
                UserDisplay userDisplay = viewPermissionService.getUserDisplayOfProject(loginUser.getPrjid(),
                        Constants.DEFAULT_VIEW_MATRIX_ID, loginUser.getUid(), srrTa.getAssignedUserId());
                request.setAttribute("respondent", userDisplay);
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

                Date dueDate = ta.getDueTime();
                if (dueDate == null || dueDate.before(new Date())) {
                    dueDate = DateUtils.nextIntervalDays(new Date(), 2);
                }
                request.setAttribute("duedate", DateUtils.date2Str(dueDate).substring(0, 10));
            }
        }

        return mapping.findForward(FWD_SUCCESS);
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
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }
}
