/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.ContentApproval;
import com.ocs.indaba.service.ContentApprovalService;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class ContentApprovalAction extends BaseAction {
    private static final Logger log = Logger.getLogger(ContentApprovalAction.class);
    
    /* forward name="success" path="" */
    private static final String PARAM_NOTE = "note";
    private static final String ATTR_CONTENT_APPROVAL = "contentApproval";

    private ContentApprovalService contentApprovalService;
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
        log.debug("********************* Enter ContentApprovalAction.");
        LoginUser loginUser = preprocess(mapping, request, response);
        
        ActionForward actionFwd = super.checkAssignmentStatus(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        
        String action = request.getParameter("action");
        if (action == null) {     // display
            taskService.updateStatusAndPercentage(loginUser.getUid(), taskAssignmentId, Constants.TASK_STATUS_NOTICED, 0);
            ContentApproval contentApproval = contentApprovalService.getContentApproval(horseId, loginUser.getUid(), taskAssignmentId);
            if (contentApproval != null) {
                request.setAttribute(ATTR_CONTENT_APPROVAL, contentApproval);
            }
            return mapping.findForward("contentApproval");
        } else {
            if (action.equals("save")) {        // save
                taskService.updateStatusAndPercentage(loginUser.getUid(), taskAssignmentId, Constants.TASK_STATUS_STARTED, (float)0.5);
            } else {                            // save and done
                taskService.updateStatusAndPercentage(loginUser.getUid(), taskAssignmentId, Constants.TASK_STATUS_DONE, 1);
            }

            int contentHeaderId = horseService.getContentHeaderByHorseId(horseId).getId();
            String note = request.getParameter(PARAM_NOTE);
            Date time = new Date();

            ContentApproval contentApproval = new ContentApproval();
            contentApproval.setUserId(loginUser.getUid());
            contentApproval.setNote(note);
            contentApproval.setTime(time);
            contentApproval.setContentHeaderId(contentHeaderId);
            contentApproval.setTaskAssignmentId(taskAssignmentId);
            contentApproval = contentApprovalService.addContentApproval(contentApproval);

            request.setAttribute(ATTR_CONTENT_APPROVAL, contentApproval);

            if (action.equals("saveAndDone")) {
                return setAssignmentCompleteMessage(mapping, request);
            } else {
                return mapping.findForward("contentApproval");
            }
        }
    }

    @Autowired
    public void setContentApprovalService(ContentApprovalService contentApprovalService) {
        this.contentApprovalService = contentApprovalService;
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
