package com.ocs.indaba.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.AssignmentService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.vo.AssignmentSubmissionResult;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import org.apache.log4j.Logger;

public class SubmitSurveyAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(SubmitSurveyAction.class);
    private SurveyService surveyService;
    private AssignmentService assignmentService;

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setAssignmentService(AssignmentService srvc) {
        this.assignmentService = srvc;
    }

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        LoginUser loginUser = preprocess(mapping, request, response);
        try {
            final PrintWriter writer = response.getWriter();
            int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);

            String type = request.getParameter("type");

            int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);

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
                if (Constants.SURVEY_ACTION_REVIEW.equalsIgnoreCase(type)) {
                    surveyService.submitSurveyReviewAssignment(loginUser.getUid(), horseId, assignId);
                } else if (Constants.SURVEY_ACTION_PEERREVIEW.equalsIgnoreCase(type)) {
                    int rc = surveyService.submitSurveyPeerReviewAssignment(loginUser.getUid(), horseId, assignId);
                    if (rc < 0) {
                        result.setCode(AssignmentSubmissionResult.RESULT_ERROR);
                        result.setMessage(this.getMessage(request, Messages.KEY_COMMON_ERR_TASK_ASSIGNMENT_NOT_EXIST));
                    } else if (rc > 0) {
                        result.setCode(AssignmentSubmissionResult.RESULT_ERROR);
                        result.setMessage(this.getMessage(request, "common.js.alert.answerleft", rc));
                    }
                } else if (Constants.SURVEY_ACTION_PRREVIEW.equalsIgnoreCase(type)) {
                    surveyService.submitSurveyPrReviewAssignment(loginUser.getUid(), horseId, assignId);
                }
            }

            this.writeRespJSON(response, result.getCode(), result.getMessage());
        } catch (IOException e) {
        }

        return null;
    }
}
