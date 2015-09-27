/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ocs.common.Config;
import com.ocs.indaba.service.WorkflowService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luke
 */
public class WorkflowConsoleAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(WorkflowConsoleAction.class);

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
        preprocess(mapping, request, response);
        
        request.setAttribute(ATTR_WORKFLOW_LIST, workflowService.getWorkflowList());
        request.setAttribute(ATTR_WORKFLOW_VIEW_LIST, workflowService.getWorkflowViewList());
        request.setAttribute(ATTR_WORKFLOW_OBJ_LIST, workflowService.getWorkflowObjectViewList());
        request.setAttribute(ATTR_RELOAD_TIME, Config.getInt(Config.KEY_WORKFLOW_RELOAD_INTERVAL));
        return mapping.findForward(FWD_SUCCESS);
    }

    @Autowired
    public void setWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }
}
