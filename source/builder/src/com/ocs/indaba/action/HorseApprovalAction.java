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

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;

import com.ocs.indaba.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

/**
 *
 * @author Luke
 */
public class HorseApprovalAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(HistoryChartAction.class);
    //private static final String PARAM_NOTE = "note";
    private static final String PARAM_HORSE_INFO = "horseInfo";

    private HorseService horseService;
    private TaskService taskService;
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
        LoginUser loginUser = preprocess(mapping, request, response);
        
        ActionForward actionFwd = super.checkAssignmentStatus(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        
        String actionDone = request.getParameter("saveAndDone");
        if (actionDone == null) {     // display
            taskService.updateStatusAndPercentage(loginUser.getUid(), taskAssignmentId, Constants.TASK_STATUS_STARTED, 0);
            request.setAttribute(PARAM_HORSE_INFO, horseService.getHorseInfo(horseId));
            return mapping.findForward("horseApproval");
        } else {
            taskService.updateStatusAndPercentage(loginUser.getUid(), taskAssignmentId, Constants.TASK_STATUS_DONE, 1);
            return mapping.findForward(FWD_SUCCESS);
        }
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }
    
    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
