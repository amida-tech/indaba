package com.ocs.indaba.action;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.vo.LoginUser;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

public class PeopleChangepwdAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(PeopleChangepwdAction.class);
    private static final String FWD_PEOPLE_PROFILE = "profile";
    private static final String PARAM_USERID = "uid";
    private static final String ATTR_USER = "user";
    private static final String ATTR_OPEN_CASE_LIST = "openCases";
    private static final String ATTR_ASSIGNED_TASKS = "assignedTasks";
    private static final String ATTR_ROLE_NAME = "roleName";
    private UserService userService;
    private CaseService caseService;
    private TaskService taskService = null;
    //private RoleService roleService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        String curpwd = (String) request.getParameter("curpwd");
        String newpwd = (String) request.getParameter("newpwd");


        String id = (String) request.getParameter(PARAM_USERID);

        int userId = Integer.valueOf(id);
        //Integer projectId = prjid;

        User user = userService.getUser(userId);
        PrintWriter out = response.getWriter();
        if (curpwd.equals(user.getPassword())) {
            user.setPassword(newpwd);
            user.setLastPasswordChangeTime(new Date());
            userService.updateUser(user);
            out.println(getMessage(request, Messages.KEY_COMMON_ALERT_CHANGEPASSWD_SUCCESS));
        } else {
            out.println(getMessage(request, Messages.KEY_COMMON_ALERT_CHANGEPASSWD_FAIL));
        }

        //set user basic info;
        request.setAttribute(ATTR_USER, userService.getUser(userId));
        request.setAttribute(ATTR_ROLE_NAME, roleService.getRoleByProjectIdAndUserId(loginUser.getPrjid(), userId));
        //set open case info;
        request.setAttribute(ATTR_OPEN_CASE_LIST,
                caseService.getOpenCasesByAssignUserId(userId));
        //set user basic info;
        request.setAttribute(ATTR_ASSIGNED_TASKS, taskService.getAssignedTasksByUserId(userId, loginUser.getPrjid()));

        return null;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
    /*
    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
    */
}
