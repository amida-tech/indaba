package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class JournalOverallReviewAction extends NoteBookAction {

    private static final Logger logger = Logger.getLogger(JournalOverallReviewAction.class);
    private static final String PARAM_DISPLAY_HAVE_QUESTIONS = "displayHaveQuestions";

    /**
     * This is the action called from the Struts framework.
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
        preprocess(mapping, request, response);

        ActionForward actionFwd = super.checkAssignmentStatus(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }
        //
        // Permitted parameters:
        //  * action   - [MUST]     the action: create, edit, display, review, peerreview, pay, approve
        //  * horseid  - [MUST]     the horse id related with this notebook
        //  * cntid    - [OPTIONAL] the journal content object id of this notebook
        //
        String action = (String) request.getParameter(ATTR_ACTION);
        if (action == null) {
            action = NOTEBOOK_ACTION_OVERALLSAVE;
        }

        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);

        int cntId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_ID), Constants.INVALID_INT_ID);

        if (horseId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }

        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        if (request.getAttribute(PARAM_ASSIGNID) != null) {
            assignId = (Integer) request.getAttribute(PARAM_ASSIGNID);
        } else {
            assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        }
        
        logger.debug("action: " + action + ", horseId: " + horseId + ", asignId: " + assignId);

        //set horse id
        request.setAttribute(PARAM_HORSE_ID, horseId);
        request.setAttribute(PARAM_ASSIGNID, request.getParameter(PARAM_ASSIGNID));


        
        TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() != Constants.TASK_STATUS_ACTIVE
                    && assignment.getStatus() < Constants.TASK_STATUS_NOTICED) {
                taskService.updateTaskAssignmentStatus(assignId, Constants.TASK_STATUS_NOTICED);
            }
        }

        int taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        int toolId = StringUtils.str2int(request.getParameter(PARAM_TOOL_ID), Constants.INVALID_INT_ID);
        int superEdit = StringUtils.str2int(request.getParameter(PARAM_SUPER_EDIT), Constants.INVALID_INT_ID);
//        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.INVALID_INT_ID);

        if (toolId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "toolid"));
            return mapping.findForward(FWD_ERROR);
        }

        request.setAttribute(ATTR_ACTION, action);
        request.setAttribute(PARAM_ASSIGNID, taskAssignmentId);
        request.setAttribute(PARAM_TOOL_ID, toolId);
        request.setAttribute(PARAM_SUPER_EDIT, superEdit);
        request.setAttribute(NOTEBOOK_EDIT_INSTRUCTION, journalService.getInstrunctionbyhorseID(horseId));
        logger.debug("ACTION: " + request.getAttribute(ATTR_ACTION));
        // Log task click event

        request.setAttribute(PARAM_DISPLAY_HAVE_QUESTIONS, !taskService.reviewResponseAssignmentExist(horseId, "journal review response"));
        actionFwd = handleDisplay(mapping, request, assignId, cntId, horseId);
        return actionFwd;
    }
    // Handle display

    private ActionForward handleDisplay(ActionMapping mapping,
            HttpServletRequest request, int assignId, int cntId, int horseId) {
        super.preHandle(request, assignId, cntId, horseId);
        return mapping.findForward(NOTEBOOK_ACTION_DISPLAY);
    }
}
