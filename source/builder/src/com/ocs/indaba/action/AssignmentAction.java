/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.service.AssignmentService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luke
 */
public class AssignmentAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(AssignmentAction.class);

    //private static final String ATTR_WORKFLOW_LIST = "workflowList";
    private AssignmentService assignmentService = null;
    
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
        int assignId = Integer.valueOf(request.getParameter("assignid"));
        String action = request.getParameter("action");
        if (action.equals("saveDeadline")) {
            String deadline = request.getParameter("deadline");
            assignmentService.changeAssignmentDeadline(assignId, deadline);
        } else if (action.equals("exitAssignment")) {
            int exitOption = Integer.valueOf(request.getParameter("exitOption"));
            assignmentService.exitAssignment(assignId, exitOption);
        }
        return null;
    }

    @Autowired
    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }
}
