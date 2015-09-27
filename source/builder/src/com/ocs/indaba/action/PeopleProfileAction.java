/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import java.util.TimeZone;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.LanguageService;
//import com.ocs.indaba.service.RoleService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.indaba.po.User;
import com.ocs.indaba.vo.LoginUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

/**
 *
 * @author menglong
 */
public class PeopleProfileAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(PeopleProfileAction.class);
    private static final String FWD_PEOPLE_PROFILE = "profile";
    private static final String PARAM_USERID = "targetUid";
    private static final String ATTR_USER = "user";
    private static final String ATTR_OPEN_CASE_LIST = "openCases";
    private static final String ATTR_ASSIGNED_TASKS = "assignedTasks";
    private static final String ATTR_ROLE_NAME = "roleName";
    private static final String ATTR_LANGUAGE = "language";
    private static final String ATTR_LANGUAGES = "languages";
    //private static final String ATTR_LANGUAGE_LIST = "languageList";
    private static final String ATTR_TIMEZONE = "timeZone";
    private static final String ATTR_LAST_LOGIN_DATE = "lastLoginDate";
    private static final String KNOW_USER = "knowUser";
    private static final String PROJECTS = "projects";
    private UserService userService;
    private CaseService caseService;
    private TaskService taskService = null;
    //private RoleService roleService;
    private LanguageService languageService;
    private ViewPermissionService viewPermisssionService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        int userId = Integer.valueOf(request.getParameter(PARAM_USERID));//he or she is the one to be seen

        //set user basic info;
        User user = userService.getUser(userId);
        if (user.getLastLoginTime() != null) {
            String lastLoginTime = user.getLastLoginTime().toString();
            if (lastLoginTime.length() > 10) {
                request.setAttribute(ATTR_LAST_LOGIN_DATE, lastLoginTime.substring(0, 10));
            }
        }
        request.setAttribute(ATTR_USER, user);
        request.setAttribute(PROJECTS, userService.getUserProjects(userId));
        request.setAttribute(ATTR_LANGUAGE, languageService.getLanguageById(user.getLanguageId()));
        request.setAttribute(ATTR_LANGUAGES, languageService.getAllLanguages());
        request.setAttribute(ATTR_ROLE_NAME, roleService.getRoleByProjectIdAndUserId(loginUser.getPrjid(), userId));
        //set view permission
        request.setAttribute(KNOW_USER, viewPermisssionService.getUserDisplayOfProject(loginUser.getPrjid(), Constants.DEFAULT_VIEW_MATRIX_ID, loginUser.getUid(), userId));
        //set open case info;
        request.setAttribute(ATTR_OPEN_CASE_LIST, caseService.getOpenCaseListByUserIdAndProjectId(userId, loginUser.getPrjid()));
        //set user basic info;
        request.setAttribute(ATTR_ASSIGNED_TASKS, taskService.getAssignedTasksByUserId(userId, loginUser.getPrjid()));
        //request.setAttribute(ATTR_LANGUAGE_LIST, languageService.getAllLanguages());

        String[] tIDs = TimeZone.getAvailableIDs(user.getTimezone()*60*60*1000);
        if (tIDs != null && tIDs.length > 0) {
            TimeZone tz = TimeZone.getTimeZone(tIDs[0]);
            request.setAttribute(ATTR_TIMEZONE, tz.getID() + " - " + tz.getDisplayName() + " / " + tz.getDisplayName(false, java.util.TimeZone.SHORT));
        }
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
    @Autowired
    @Override
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
     *
     */

    @Autowired
    public void setLanguageService(LanguageService languageService) {
        this.languageService = languageService;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermisssionService) {
        this.viewPermisssionService = viewPermisssionService;
    }
}
