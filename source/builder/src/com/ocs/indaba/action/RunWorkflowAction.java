/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.util.StringUtils;
import com.ocs.common.Config;
import com.ocs.indaba.service.WorkflowService;

//import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
/**
 *
 * @author luke
 */
public class RunWorkflowAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(RunWorkflowAction.class);

    private static final String ATTR_WORKFLOW_LIST = "workflowList";
    private static final String ATTR_WORKFLOW_VIEW_LIST = "workflowViewList";
    private static final String ATTR_WORKFLOW_OBJ_LIST = "workflowObjectList";
    private static final String ATTR_RELOAD_TIME = "reloadTime";

    private WorkflowService workflowService = null;

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
        String returnStr;
        int wfoId = StringUtils.str2int(request.getParameter("wfoid"), Constants.INVALID_INT_ID);
        if (wfoId != Constants.INVALID_INT_ID) {
            returnStr = workflowService.runWorkflowObject(wfoId);
        } else {
            int workflowId = StringUtils.str2int(request.getParameter("id"), Constants.INVALID_INT_ID);
            if (workflowId != Constants.INVALID_INT_ID) {
                returnStr = workflowService.runWorkflow(workflowId);
            } else {
                returnStr = workflowService.runAllWorkflow();
            }
        }

        if (request.getParameter("disp") != null) {
            request.setAttribute(ATTR_WORKFLOW_LIST, workflowService.getWorkflowList());
            request.setAttribute(ATTR_WORKFLOW_OBJ_LIST, workflowService.getWorkflowObjectViewList());
            request.setAttribute(ATTR_WORKFLOW_VIEW_LIST, workflowService.getWorkflowViewList());
            request.setAttribute(ATTR_RELOAD_TIME, Config.getInt(Config.KEY_WORKFLOW_RELOAD_INTERVAL));
            request.setAttribute("returnStr", returnStr);
            return mapping.findForward(FWD_SUCCESS);
        }
        return null;
    }

    @Autowired
    public void setWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }
}
