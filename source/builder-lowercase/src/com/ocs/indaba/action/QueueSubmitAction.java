/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.QueueService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.QueueSubmitResultView;
import com.ocs.indaba.vo.QueueTaskAssignment;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class QueueSubmitAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(QueueSubmitAction.class);
    private static final int APPLY_TYPE = 1;//assign to me
    private static final int RETURN_TYPE = 2;//return to queue
    private static final int ADMIN_TYPE = 3;//admin update assignment
    private static final int SUBMIT_FALIED = 0;
    private static final int SUBMIT_SUCCESS = 1;
    private static final int SUBMIT_NO_PRIORITY = 2;
    private static final int SUBMIT_STATUS_CHANGE = 3;//use for the situation that two people submit at the same time
    //private static final int SUBMIT_BAD_PARAMETER = 4;
    private QueueService queueService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        if (!verifyParameters(request)) {
            try {
                writeMsgUTF8(response, getMessage(request, Messages.KEY_COMMON_ERR_BADPARAM));
            } catch (IOException ex) {
                logger.error("bad parameters from client", ex);
            }
            return null;
        }
        int type = StringUtils.str2int(request.getParameter("type"), 0);
        JsonObject obj = null;
        if (type == APPLY_TYPE) {
            int taskAssigmentId = StringUtils.str2int(request.getParameter("taId"), 0);
            obj = getApplyJson(loginUser, taskAssigmentId);
        } else if (type == RETURN_TYPE) {
            int taskAssigmentId = StringUtils.str2int(request.getParameter("taId"), 0);
            obj = getReturnJson(loginUser, taskAssigmentId);
        } else if (type == ADMIN_TYPE) {
            int taskAssigmentId = StringUtils.str2int(request.getParameter("taId"), 0);
            int selectUserId = StringUtils.str2int(request.getParameter("uid"), -1);
            int selectPriority = StringUtils.str2int(request.getParameter("pid"), 0);
            obj = getUpdateJson(loginUser, taskAssigmentId, selectUserId, selectPriority);
        }
        try {
            writeMsgUTF8(response, new Gson().toJson(obj));
        } catch (IOException ex) {
            logger.error("failed to send message to client", ex);
        }
        return null;
    }

    @Autowired
    public void setQueueService(QueueService queueService) {
        this.queueService = queueService;
    }

    private boolean verifyParameters(HttpServletRequest request) {
        int type = StringUtils.str2int(request.getParameter("type"), 0);
        if ((type != APPLY_TYPE) && (type != RETURN_TYPE) && (type != ADMIN_TYPE)) {
            return false;
        }
        int taskAssignmentId = StringUtils.str2int(request.getParameter("taId"), 0);
        if (taskAssignmentId == 0) {
            return false;
        }
        if (type == ADMIN_TYPE) {
            int selectUserId = StringUtils.str2int(request.getParameter("uid"), -1);
            int selectPriority = StringUtils.str2int(request.getParameter("pid"), 0);
            if (selectUserId == -1) {
                return false;
            }
            if (selectPriority > 0 && selectPriority <= QueueService.PRIORITY_NAME.length) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    private JsonObject generateJson(int code, String msg) {
        JsonObject obj = new JsonObject();
        obj.addProperty("code", code);
        obj.addProperty("msg", msg);
        return obj;
    }

    private JsonObject getApplyJson(LoginUser loginUser, int taskAssigmentId) {
        if (!queueService.canApplyTaskAssignment(loginUser.getPrjid(), loginUser.getUid(), taskAssigmentId)) {
            // no priority to apply, should identify if the taskAssignment has been assigned to sb.
            QueueTaskAssignment view = queueService.getTaskAssignmentView(loginUser.getUid(), loginUser.getLanguageId(), taskAssigmentId);
            if (view.isAssigned()) {
                JsonObject obj = generateJson(SUBMIT_STATUS_CHANGE, "this taskAssignment has been assigned yet!");
                String assignHtml = Messages.getInstance().getMessage(Messages.KEY_JSP_QUEUES_ASSIGNTO, loginUser.getLanguageId()) 
                        + " <a target=\"_blank\" href=\"profile.do?targetUid="
                        + view.getAssignedUserId() + "\">" + view.getAssignedUserName() + "</a>";
                obj.addProperty("assignHtml", assignHtml);
                return obj;
            } else {
                return generateJson(SUBMIT_NO_PRIORITY, "you can't get this taskAssignment from queue");
            }
        }
        QueueSubmitResultView resultView = queueService.submitApplyTaskAssignment(loginUser.getUid(), taskAssigmentId);
        if (resultView != null) {
            JsonObject obj = generateJson(SUBMIT_SUCCESS, "update success");
            String assignHtml = Messages.getInstance().getMessage(Messages.KEY_JSP_QUEUES_ASSIGNTO, loginUser.getLanguageId()) 
                        + " <a target=\"_blank\" href=\"profile.do?targetUid="
                    + resultView.getAssignedUserId() + "\">" + resultView.getAssignedUserName() + "</a>"
                    + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;(<a href=\"#\" onclick=\"returnToQueue("
                    + taskAssigmentId + ")\">"
                    + Messages.getInstance().getMessage(Messages.KEY_JSP_QUEUES_RETURNTOQUEUE, loginUser.getLanguageId())
                    + "</a>)";
            obj.addProperty("assignHtml", assignHtml);
            return obj;
        } else {
            return generateJson(SUBMIT_FALIED, "server error");
        }
    }

    private JsonObject getReturnJson(LoginUser loginUser, int taskAssigmentId) {
        if (!queueService.canReturnTaskAssignment(loginUser.getUid(), taskAssigmentId)) {
            return generateJson(SUBMIT_NO_PRIORITY, "you can't return this taskAssignment to queue");
        }
        boolean sucess = queueService.submitReturnTaskAssignment(loginUser.getUid(), taskAssigmentId);
        if (sucess) {
            JsonObject obj = generateJson(SUBMIT_SUCCESS, "update success");
            String assignHtml = "<a href=\"#\" onclick=\"return assignToMe("
                    + taskAssigmentId + ")\">"
                    + Messages.getInstance().getMessage(Messages.KEY_JSP_QUEUES_IWANTTHIS, loginUser.getLanguageId())
                    + "</a>";
            obj.addProperty("assignHtml", assignHtml);
            return obj;
        } else {
            return generateJson(SUBMIT_FALIED, "server error");
        }
    }

    private JsonObject getUpdateJson(LoginUser loginUser, int taskAssigmentId, int selectUserId, int selectPriority) {
        if (!queueService.canUpdateTaskAssignment(loginUser.getPrjid(), loginUser.getUid(), taskAssigmentId, selectUserId)) {
            return generateJson(SUBMIT_NO_PRIORITY, "you can't update this taskAssignment");
        }
        QueueSubmitResultView resultView = queueService.submitUpdateTaskAssignment(taskAssigmentId, selectUserId, selectPriority);
        if (resultView != null) {
            JsonObject obj = generateJson(SUBMIT_SUCCESS, "update success");
            String assignHtml;
            if (selectUserId != 0) {
                assignHtml = Messages.getInstance().getMessage(Messages.KEY_JSP_QUEUES_ASSIGNTO, loginUser.getLanguageId()) 
                        + " <a target=\"_blank\" href=\"profile.do?targetUid="
                        + resultView.getAssignedUserId() + "\">" + resultView.getAssignedUserName() + "</a>";
            } else {
                assignHtml = Messages.getInstance().getMessage(Messages.KEY_JSP_QUEUES_NOASSIGN, loginUser.getLanguageId());
            }
            obj.addProperty("assignHtml", assignHtml);
            obj.addProperty("priorityHtml", resultView.getPriority());
            return obj;
        } else {
            return generateJson(SUBMIT_FALIED, "server error");
        }
    }
}
