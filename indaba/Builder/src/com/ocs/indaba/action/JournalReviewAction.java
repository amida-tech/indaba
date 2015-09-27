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

public class JournalReviewAction extends NoteBookAction {

    private static final Logger logger = Logger.getLogger(JournalReviewAction.class);
    private static final String PARAM_DISPLAY_HAVE_QUESTIONS = "displayHaveQuestions";
    /*private NoteBookService notebookService = null;
    private AssignmentService assignmentService = null;*/
    //private JournalService journalService = null;

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
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int cntId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_ID), Constants.INVALID_INT_ID);

        if (horseId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }

        final int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        //set horse id
        request.setAttribute(PARAM_HORSE_ID, horseId);
        request.setAttribute(PARAM_ASSIGNID, assignId);
        TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_NOTICED) {
                taskService.updateTaskAssignmentStatus(assignId, Constants.TASK_STATUS_NOTICED);
            }
        }

        request.setAttribute(PARAM_DISPLAY_HAVE_QUESTIONS, !taskService.reviewResponseAssignmentExist(horseId, "journal review response"));

        actionFwd = handleDisplay(mapping, request, assignId, cntId, horseId);
        return actionFwd;
    }
    // Handle display

    private ActionForward handleDisplay(ActionMapping mapping,
            HttpServletRequest request, int assignId, int cntId, int horseId) {
        preHandle(request,assignId, cntId, horseId);
        return mapping.findForward(NOTEBOOK_ACTION_DISPLAY);
    }

}
