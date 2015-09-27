/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class SurveyOverallReviewAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyOverallReviewAction.class);
    private TaskService taskService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        preprocess(mapping, request, response, true, true);
        ActionForward actionFwd = super.checkAssignmentStatus(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        int horseId = StringUtils.str2int(request.getParameter(ATTR_HORSE_ID), Constants.INVALID_INT_ID);
        if (horseId < 0) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }

        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        if (assignId < 0) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "assignid"));
            return mapping.findForward(FWD_ERROR);
        }

        request.setAttribute(ATTR_HORSE_ID, horseId);
        request.setAttribute(PARAM_ASSIGNID, assignId);
        request.setAttribute(ATTR_ACTION, getActionPath(request));


        TaskAssignment ta = taskService.getTaskAssignment(assignId);
        if (ta != null && ta.getStatus() == Constants.TASK_STATUS_AWARE) {
            taskService.updateTaskAssignmentStatus(assignId, Constants.TASK_STATUS_STARTED);
        }

        return mapping.findForward(FWD_SUCCESS);
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
