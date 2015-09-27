/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.QueueService;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.util.ListUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.QueueTask;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author menglong luwb
 */
public class QueueAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(QueueAction.class);
    private QueueService queueService;
    private static final String FWD_QUEUES = "queues";
    private static final String FWD_QUEUE_TASKLIST = "queueTaskList";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        request.setAttribute(Constants.COOKIE_ACTIVE_TAB, Constants.TAB_QUEUES);
        CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_QUEUES);
//
//        List<Queue> editingList = queueService.getEditingQueues();
//        List<Queue> finalApprovalList = queueService.getFinalApprovalQueues();
//        List<Queue> paymentList = queueService.getPaymentQueues();
//
//        int editingAverageTime = calcAverageTime(editingList);
//        int approvalAverageTime = calcAverageTime(finalApprovalList);
//        int paymentAverageTime = calcAverageTime(paymentList);
//
//        List<User> userList = userManager.getAllUser();
//
//        session.setAttribute(ATTR_ACTIVE, TAB_QUEUES);
//        request.setAttribute(ATTR_EDITING_LIST, editingList);
//        request.setAttribute(ATTR_EDITING_AVE_TIME, editingAverageTime);
//        request.setAttribute(ATTR_FINAL_APPROVAL_LIST, finalApprovalList);
//        request.setAttribute(ATTR_APPROVAL_AVE_TIME, approvalAverageTime);
//        request.setAttribute(ATTR_PAYMENT_LIST, paymentList);
//        request.setAttribute(ATTR_PAYMENT_AVE_TIME, paymentAverageTime);
//        request.setAttribute(ATTR_USER_LIST, userList);
//        request.setAttribute(ATTR_PRIORITY_LIST, ConfigMock.generatePriorityList());
        List<QueueTask> queueTaskList = queueService.getQueueTasks(loginUser.getPrjid(), loginUser.getUid(), loginUser.getLanguageId());
        if (!ListUtils.isEmptyList(queueTaskList)) {
            request.setAttribute(FWD_QUEUE_TASKLIST, queueTaskList);
        }
        return mapping.findForward(FWD_QUEUES);
    }

    @Autowired
    public void setQueueService(QueueService queueService) {
        this.queueService = queueService;
    }
}
