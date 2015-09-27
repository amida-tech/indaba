package com.ocs.indaba.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.vo.SurveyPeerReviewVO;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;


/*
 * This Action is no longer in use. Survey tree is displayed with the SurveyTreeTagHandler.
 */

public class ScorecardPeerReviewDisagreementListAction extends BaseAction {
    private static final String ATTR_INDICATORS = "indicators";
    private SurveyService surveyService;
    private TaskService taskService;

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
            this.surveyService = surveyService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
            this.taskService = taskService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);

        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
        if ((assignedTask != null) && (assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW
                || assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_PR_REVIEW)) {
            List<SurveyPeerReviewVO> indicators = surveyService.getPeerReviewDisagreementList(assignedTask.getHorseId());
            request.setAttribute(ATTR_INDICATORS, indicators);
        }
        
        return mapping.findForward(FWD_SUCCESS);
    }
}
