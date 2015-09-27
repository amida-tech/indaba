/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

//import com.ocs.indaba.service.ContentService;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.HorseService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
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
public class TaskAction extends com.ocs.indaba.action.BaseAction {

    private static final Logger logger = Logger.getLogger(TaskAction.class);
    /*
     * private NoteBookService notebookService = null; private AssignmentService
     * assignmentService = null; private NoteBookMessageService nbmsgService =
     * null;
     */
    private HorseService horseService = null;
    //private String JOURNAL_CONFIG = "journalConfig";

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, false, false);

        ActionForward actionFwd = super.checkAssignmentStatus(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        int taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        int toolId = StringUtils.str2int(request.getParameter(PARAM_TOOL_ID), Constants.INVALID_INT_ID);
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int taskType = StringUtils.str2int(request.getParameter(ATTR_TASK_TYPE), Constants.INVALID_INT_ID);


        logger.debug("Joural create request: [userId=" + loginUser.getUid() + ", projectId=" + loginUser.getPrjid() + ", assignId=" + taskAssignmentId + ", toolId=" + toolId + "].");

        if (toolId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "toolid"));
            return mapping.findForward(FWD_ERROR);
        }

        if (horseId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }

        if (taskType == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "tasktype"));
            return mapping.findForward(FWD_ERROR);
        }

        String fwd = null;
        request.setAttribute(PARAM_ASSIGNID, taskAssignmentId);
        request.setAttribute(PARAM_TOOL_ID, toolId);
        request.setAttribute(ATTR_ACTION, Constants.ACTION_MAP.get(taskType));
        request.setAttribute(ATTR_TASK_TYPE, taskType);
        //request.setAttribute(JOURNAL_CONFIG, journalService.getJournalConfigbyHorseId(horseId));
        int cntType = horseService.getContentTypeByHorseId(horseId);
        switch (cntType) {
            case Constants.CONTENT_TYPE_SURVEY:
                fwd = FWD_SURVEY;
                break;
            case Constants.CONTENT_TYPE_JOURNAL:
                fwd = FWD_NOTEBOOK;
                break;
        }

        // Log task click event
        logEventByKey(loginUser.getUid(), Constants.DEFAULT_EVENTLOG_ID, Constants.EVENT_TYPE_CLICK_ASSIGNMENT,
                "User click the assigned task. [assignid={0}][toolid={1}][prdtype={2}]", taskAssignmentId, toolId, cntType);
        return mapping.findForward(fwd);
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }
}
