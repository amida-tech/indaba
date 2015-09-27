/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.ContentPayment;
import com.ocs.indaba.service.ContentPaymentService;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class ContentPaymentAction extends BaseAction {
    
    /* forward name="success" path="" */
    private static final String PARAM_NOTE = "note";
    private static final String PARAM_AMOUNT = "amount";
    private static final String PARAM_PAYEES = "payees";
    private static final String ATTR_CONTENT_PAYMENT = "contentPayment";
    private ContentPaymentService contentPaymentService;
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

        String action = request.getParameter("action");
        if (action == null) {                   // display
            taskService.updateStatusAndPercentage(loginUser.getUid(), taskAssignmentId, Constants.TASK_STATUS_NOTICED, 0);
            ContentPayment contentPayment = contentPaymentService.getContentPayment(horseId, loginUser.getUid(), taskAssignmentId);
            if (contentPayment == null) {
                contentPayment = new ContentPayment();
                contentPayment.setTime(new Date());
            }
            request.setAttribute(ATTR_CONTENT_PAYMENT, contentPayment);
            return mapping.findForward("contentPayment");
        } else {
            if (action.equals("save")) {        // save
                request.setAttribute("getTime", new Date().toLocaleString());
                taskService.updateStatusAndPercentage(loginUser.getUid(), taskAssignmentId,
                        Constants.TASK_STATUS_STARTED, (float)0.5);
            } else {                            // save and done
                taskService.updateStatusAndPercentage(loginUser.getUid(), taskAssignmentId,
                        Constants.TASK_STATUS_DONE, 1);
            }
            int contentHeaderId = horseService.getContentHeaderByHorseId(horseId).getId();
            String note = request.getParameter(PARAM_NOTE);
            String amount = request.getParameter(PARAM_AMOUNT);
            String payees = request.getParameter(PARAM_PAYEES);

            ContentPayment contentPayment = new ContentPayment();
            contentPayment.setPaidByUserId(loginUser.getUid());
            contentPayment.setAmount(new BigDecimal(amount));
            contentPayment.setNote(note);
            contentPayment.setTime(new Date());
            contentPayment.setContentHeaderId(contentHeaderId);
            contentPayment.setTaskAssignmentId(taskAssignmentId);
            contentPayment.setPayees(payees);

            contentPayment = contentPaymentService.addContentPayment(contentPayment);
            request.setAttribute(ATTR_CONTENT_PAYMENT, contentPayment);
            if (action.equals("saveAndDone")) {
                return setAssignmentCompleteMessage(mapping, request);
            } else {
                return mapping.findForward("contentPayment");
            }
        }
    }

    @Autowired
    public void setContentPaymentService(ContentPaymentService contentPaymentService) {
        this.contentPaymentService = contentPaymentService;
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
