/**
 * 
 */
package com.ocs.indaba.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import org.apache.log4j.Logger;

/**
 * @author Tiger Tang
 *
 */
public class UpdateTaskAssignmentStatusAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(UpdateTaskAssignmentStatusAction.class);

    private TaskService taskService;

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        LoginUser loginUser = preprocess(mapping, request, response);
        
        try {
            final PrintWriter writer = response.getWriter();
            /*
            if (preprocess(mapping, request) != null) {
            writer.write("Invalid user!");
            return null;
            }
             */
            String action = request.getParameter("action");

            int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
            int status = StringUtils.str2int(request.getParameter("status"), Constants.INVALID_INT_ID);
            float percentage = StringUtils.str2float(request.getParameter("percentage"), Constants.INVALID_FLOAT_ID);

            if ("updateStatus".equals(action)) {
                taskService.updateTaskAssignmentStatus(assignId, status);
                writer.write(new Gson().toJson(0));
            } else if ("updateStatusAndPercentage".equals(action)) {
                taskService.updateStatusAndPercentage(loginUser.getUid(), assignId, status, percentage);
                writer.write(new Gson().toJson(0));
            }
        } catch (IOException e) {
        }

        return null;
    }
}
