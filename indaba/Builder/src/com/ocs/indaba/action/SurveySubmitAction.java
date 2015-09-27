/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.google.gson.Gson;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.service.AssignmentService;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.SiteMessageService;
import com.ocs.indaba.service.SurveyAnswerService;
import com.ocs.indaba.service.SurveyCategoryService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.UserService;
//import com.ocs.indaba.service.AssignmentService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.AssignmentSubmissionResult;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.ProjectUserView;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class SurveySubmitAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveySubmitAction.class);
    private TaskService taskService;
    private SurveyCategoryService surveyCategoryService = null;
    private HorseService horseService = null;
    private UserService userService = null;
    private SiteMessageService siteMsgSrvc = null;
    private SurveyService surveyService;
    private SurveyAnswerService surveyAnswerService = null;
    private AssignmentService assignmentService;
    private ProjectDAO projectDao = null;
    //private AssignmentService assignmentService;
    //private static final String PARAM_SURVEY_CATEGORY_ID = "catid";
    //private static final String FWD_SURVEY_DISPLAY = "surveyDisplay";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);
        
        int horseId = StringUtils.str2int(request.getParameter(ATTR_HORSE_ID), Constants.INVALID_INT_ID);
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);

        request.setAttribute(ATTR_RET_URL, request.getParameter(ATTR_RET_URL));
        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);

        AssignmentSubmissionResult result = new AssignmentSubmissionResult();
        result.setCode(AssignmentSubmissionResult.RESULT_OK);

        // check for standing flags
        if (assignmentService.horseHasStandingFlagsAssignedToUser(loginUser.getUid(), horseId)) {
            result.setCode(AssignmentSubmissionResult.RESULT_ERROR);
            result.setMessage(this.getMessage(request, Messages.KEY_COMMON_ERR_CANT_SUBMIT_TA_FLAGS_ASSIGNED));
        } else if (assignmentService.horseHasStandingFlagsRaisedByUser(loginUser.getUid(), horseId)) {
            result.setCode(AssignmentSubmissionResult.RESULT_ERROR);
            result.setMessage(this.getMessage(request, Messages.KEY_COMMON_ERR_CANT_SUBMIT_TA_FLAGS_RAISED));
        } else {
            if ((assignedTask != null)
                    && (assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE)) {
                int userId = assignedTask.getUserId();

                ProjectUserView user = userService.getProjectUserView(loginUser.getPrjid(), userId);
                if (user != null) {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("username", user.getFirstName() + " " + user.getLastName());
                    ContentHeader ch = horseService.getContentHeaderByHorseId(horseId);
                    parameters.put("contenttitle", ch.getTitle());
                    Project project = projectDao.selectProjectByHorseId(horseId);
                    parameters.put("projectname", project.getCodeName());
                    siteMsgSrvc.deliver(user, Constants.NOTIFICATION_TYPE_SYS_REVIEW_RESPONSE_POSTED, parameters);
                    taskService.removeTaskAssignment(assignId);
                }
            } else {
                if (surveyCategoryService.getSurveyQuestionCountByHorseId(horseId)
                        == surveyCategoryService.getCompletedSurveyAnswerCountByHorseId(horseId)) {

                    /* No longer needed. CR 936 creates new version via stored proc.
                    if (assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_CREATE) {
                    surveyAnswerService.copySurveyAnswers(horseId, assignId, loginUser.getUid());
                    }
                     */

                    TaskAssignment ta = taskService.getTaskAssignment(assignId);
                    if (ta != null) {
                        ta.setStatus((short) Constants.TASK_STATUS_DONE);
                        ta.setPercent(1.0001f);
                        ta.setCompletionTime(new Date());
                        taskService.updateTaskAssignment(ta);
                    }
                    //taskService.updateTaskAssignment(assignId, Constants.TASK_STATUS_DONE, 1);
                    surveyService.resetSurveyAnswerFlag(assignId, horseId);
                }
            }
            // Fix Bug 552: save submit_time when firstly submiting all survey answers
            ContentHeader cnthdr = horseService.getContentHeaderByHorseId(horseId);
            if (cnthdr.getAuthorUserId() == 0) {
                cnthdr.setAuthorUserId(loginUser.getUid());
            }
            if (cnthdr.getSubmitTime() == null) {
                cnthdr.setSubmitTime(new Date());
            }
            cnthdr.setLastUpdateUserId(loginUser.getUid());
            cnthdr.setLastUpdateTime(new Date());
            horseService.saveContentHeader(cnthdr);
        }

        this.writeRespJSON(response, result.getCode(), result.getMessage());

        return null;
    }

    /*
    @Autowired
    public void setAssignmentService(AssignmentService assignmentService) {
    this.assignmentService = assignmentService;
    }
     *
     */
    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setSurveyCategoryService(SurveyCategoryService surveyCategoryService) {
        this.surveyCategoryService = surveyCategoryService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;

    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMsgSrvc = siteMessageService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setSurveyAnswerService(SurveyAnswerService surveyAnswerService) {
        this.surveyAnswerService = surveyAnswerService;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    @Autowired
    public void setAssignmentService(AssignmentService srvc) {
        this.assignmentService = srvc;
    }
}
