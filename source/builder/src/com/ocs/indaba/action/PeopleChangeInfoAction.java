package com.ocs.indaba.action;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.vo.LoginUser;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

public class PeopleChangeInfoAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(PeopleChangeInfoAction.class);
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

        String firstName = (String) request.getParameter("firstName");
        String lastName = (String) request.getParameter("lastName");
        if (firstName.length() == 0 && lastName.length() == 0) {
            firstName = getMessage(request, Messages.KEY_COMMON_MSG_FNAME_ANONYMOUS);
            lastName = getMessage(request, Messages.KEY_COMMON_MSG_LNAME_CONTRIBUTOR);
        }
        String email = (String) request.getParameter("email");
        String phone = (String) request.getParameter("phone");
        String cellPhone = (String) request.getParameter("cellPhone");
        String address = (String) request.getParameter("address");
        String bio = (String) request.getParameter("bio");
        String location = (String) request.getParameter("location");
        String emailDetailLevel = (String) request.getParameter("emaillevel");
        int languageId = Integer.parseInt(request.getParameter("languageId"));
        String forwardMsg = (String) request.getParameter("forwardmsg");

        String id = (String) request.getParameter(PARAM_USERID);

        int userId = Integer.valueOf(id);
        int projectId = loginUser.getPrjid();

        User user = userService.getUser(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setCellPhone(cellPhone);
        user.setAddress(address);
        user.setBio(bio);
        user.setLocation(location);
        user.setLanguageId(languageId);
        user.setForwardInboxMsg(forwardMsg.equals("1"));
        if (emailDetailLevel != null && emailDetailLevel.length() > 0) {
            user.setEmailDetailLevel(Short.parseShort(emailDetailLevel));
        }
        user.setPrivacyPolicyAcceptTime(new Date());
        userService.updateUser(user);

        //set user basic info;
        request.setAttribute(ATTR_USER, userService.getUser(userId));
        request.setAttribute(ATTR_ROLE_NAME, roleService.getRoleByProjectIdAndUserId(projectId, userId));
        //set open case info;
        request.setAttribute(ATTR_OPEN_CASE_LIST,
                caseService.getOpenCasesByAssignUserId(userId));
        //set user basic info;
        request.setAttribute(ATTR_ASSIGNED_TASKS, taskService.getAssignedTasksByUserId(userId, loginUser.getPrjid()));

        return mapping.findForward(FWD_PEOPLE_PROFILE);
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
     * @Autowired public void setRoleService(RoleService roleService) {
     * this.roleService = roleService; }
     *
     */
}
